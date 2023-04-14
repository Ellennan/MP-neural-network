package main;

import java.util.*;

import com.github.javaparser.ast.expr.*;

public class Neuron {
    public static Map<Integer, Integer> treeEdges = new HashMap<>();
    private Expression expression;
    private Neuron father;
    private int weight = 1;
    private int threshold;
    private int layer;
    private List<Neuron> input;

    public Neuron(Expression expression, Neuron father, int layer) {
        this.expression = expression;
        this.father = father;
        this.layer = layer;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setInput(List<Neuron> input) {
        this.input = input;
    }

    public void replace(Neuron newFirst, Neuron newSecond) {
        input.set(0, newFirst);
        input.add(1, newSecond);
    }

    public void parse() {

        Expression root = expression;

        // Enclosed expression with brackets
        if (root instanceof EnclosedExpr) {
            root = ((EnclosedExpr) root).getInner();
        }

        // Binary expression
        if (root instanceof BinaryExpr) {
            String operator = ((BinaryExpr) root).getOperator().toString();

            // Set threshold
            if (operator.equals("AND")) {
                setThreshold(2);
            } else {
                setThreshold(1);
            }

            // Set weight
            setWeight(1);

            // Create left and right neuron
            Expression leftChild = ((BinaryExpr) root).getLeft();
            Expression rightChild = ((BinaryExpr) root).getRight();

            Neuron leftNeuron = new Neuron(leftChild, this, layer + 1);
            Neuron rightNeuron = new Neuron(rightChild, this, layer + 1);

            // Set input
            List<Neuron> list = new LinkedList<>();
            list.add(leftNeuron);
            list.add(rightNeuron);
            setInput(list);

            // Iterate input
            int ret = 0;
            while (ret == 0) {
                Neuron firstNeuron = input.get(0);
                Neuron secondNeuron = input.get(1);
                secondNeuron.parse();
                ret = firstNeuron.parseLeft();
                if (ret == 0 && threshold > 1) {
                    threshold += 1;
                } else {
                    firstNeuron.parse();
                }
            }
        }

        // Unary expression
        if (root instanceof UnaryExpr) {
            // Set threshold & weight
            setThreshold(0);
            setWeight(-1);

            // Create child neuron
            Expression child = ((UnaryExpr) root).getExpression();
            Neuron childNeuron = new Neuron(child, this, layer + 1);
            childNeuron.parse();

            // Set input
            List<Neuron> list = new LinkedList<>();
            list.add(childNeuron);
            setInput(list);
        }

    }

    public int parseLeft() {

        Expression root = expression;

        // Enclosed expression with brackets
        if (root instanceof EnclosedExpr) {
            root = ((EnclosedExpr) root).getInner();
        }

        // Check the same operator
        if (root instanceof BinaryExpr) {
            String operator = ((BinaryExpr) root).getOperator().toString();
            if (operator.equals("AND") && father.getThreshold() > 1 || operator.equals("OR") && father.getThreshold() == 1) {
                // Create left and right neuron
                Expression leftChild = ((BinaryExpr) root).getLeft();
                Expression rightChild = ((BinaryExpr) root).getRight();

                Neuron leftNeuron = new Neuron(leftChild, father, layer);
                Neuron rightNeuron = new Neuron(rightChild, father, layer);

                father.replace(leftNeuron, rightNeuron);

                return 0;
            }
        }

        return 1;
    }

    public void visualize() {
        if (input != null) {

            if (father != null) {
                ASTFormer(layer);
                System.out.println("|");
                ASTFormer(layer);
                System.out.print("|__" + father.getWeight() + "__*" + threshold + "\n");
            } else {
                System.out.println("|");
                System.out.println("|");
                System.out.print("*" + threshold + "\n");
            }

            for (Neuron neuron : input) {
                int otherChildren = input.size() - input.indexOf(neuron) - 1;
                treeEdges.put(neuron.layer, otherChildren);
                neuron.visualize();
            }
        } else {
            ASTFormer(layer);
            System.out.println("|");
            ASTFormer(layer);
            System.out.print("|__" + father.getWeight() + "__" + expression.toString() + "\n");
        }
    }

    public void ASTFormer(int layer) {
        for (int i = 0; i < layer; i++) {
            if (treeEdges.get(i) != null) {
                if (treeEdges.get(i) > 0) {
                    System.out.print("|");
                    for (int j = 0; j < 6; j++) {
                        System.out.print(" ");
                    }
                } else {
                    for (int j = 0; j < 7; j++) {
                        System.out.print(" ");
                    }
                }
            }
        }
    }
}
