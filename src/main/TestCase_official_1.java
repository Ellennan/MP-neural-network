package main;

public class TestCase_official_1 {
    boolean a, b, c, y0, prq;

//    boolean x = a && (!(b || c && prq) || y0);

    boolean x = a && !b && (c||b);
}
