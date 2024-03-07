
/*
 * CPSC501 Assignment 2 Submission
 * Brett Gattinger
 * 30009390
 */


public class TestClassB {
    public int attribute_G1;
    public float attribute_G2;
    public char attribute_G3;

    TestClassB (int d1, float d2, char d3) {
        this.attribute_G1 = d1;
        this.attribute_G2 = d2;
        this.attribute_G3 = d3;
    }

    public void method_D1(int param1) {
        System.out.println("this is method D1");
    }

    protected void method_E1(int param1) {
        System.out.println("this is method E1");
    }

    private void method_F1() {
        System.out.println("this is method F1");
    }
}
