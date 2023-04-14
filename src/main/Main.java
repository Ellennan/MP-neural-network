package main;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.printer.DefaultPrettyPrinter;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.FileInputStream;


public class Main {

    // Read the file with the SAME path of the main class.
    // Please put the all the test cases in the same path of the main class in your project too.

    // Test the expression of which the root of the AST is a binary expression.
    // private static final String FILE_PATH = "src/main/Example1.java";

    // Test the expression of which the root of the AST is a unary expression.
//    private static final String FILE_PATH = "src/main/Example2.java";
    private static final String FILE_PATH = "src/main/TestCase_official_1.java";


    public static void main(String[] args) throws Exception {
        // Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));

        // Find the first recognized expression, which is the boolean propositional logic formula.
        Expression root = cu.findAll(Expression.class).get(0);

        Neuron neuron = new Neuron(root, null,-1);
        neuron.parse();

        neuron.visualize();

        System.out.println(Neuron.treeEdges);


        // print out the child (children), and the operator
        // Note that the expression can be binary or unary
        // &&, || are binary expression, while ! is unary expression

         /*if (root instanceof BinaryExpr) {
            System.out.println(((BinaryExpr) root).getLeft().toString());
            System.out.println(((BinaryExpr) root).getRight().toString());
            System.out.println(((BinaryExpr) root).getOperator().toString());
            Expression child = ((BinaryExpr) root).getLeft();
            System.out.println(((BinaryExpr) child).getLeft().toString());
        }
        if (root instanceof UnaryExpr) {
            System.out.println(((UnaryExpr) root).getOperator());
            System.out.println(((UnaryExpr) root).getExpression());

            Expression child = ((UnaryExpr) root).getExpression();
            // for each node, also need to check whether it is enclosed expression with brackets
            if (child instanceof EnclosedExpr) {
                System.out.println(((EnclosedExpr) child).getInner());
            }
            if (root instanceof BinaryExpr) {
                // TODO: Do what you want to do for a binary expression
            }
            if (root instanceof UnaryExpr) {
                // TODO: Do what you want to do for a unary expression
            }
        }*/
    }
}
