
/*
 * CPSC501 Assignment 2 Submission
 * Brett Gattinger
 * 30009390
 */


public class TestClassA extends TestClassSuperClass implements TestClassInterface {
        public byte attribute_A1;
        public short attribute_A2;
        public int attribute_A3;
        public long attribute_A4;
        public float attribute_A5;
        public double attribute_A6;
        public boolean attribute_A7;
        public char attribute_A8;
        public int[] attribute_A9;
        public int[][] attribute_A10;
        public TestClassB[] attribute_A11;

        public TestClassB bObject; 
        
        protected int attribute_B1;
        protected float attribute_B2;
        protected char attribute_B3;

        private int attribute_C1;
        private float attribute_C2;
        private char attribute_C3;

        TestClassA() {
            // default constructor
        }

        //public primitive only constructor
        TestClassA(byte a1, short a2, int a3, long a4,
            float a5, double a6, boolean a7, char a8, int[] a9) {
                this.attribute_A1 = a1;
                this.attribute_A2 = a2;
                this.attribute_A3 = a3;
                this.attribute_A4 = a4;
                this.attribute_A5 = a5;
                this.attribute_A6 = a6;
                this.attribute_A7 = a7;
                this.attribute_A8 = a8;
                this.attribute_A9 = a9;
        }

        //public primitive with nested object constructor
        TestClassA(byte a1, short a2, int a3, long a4,
            float a5, double a6, boolean a7, char a8, int[] a9, TestClassB bObj) {
                this.attribute_A1 = a1;
                this.attribute_A2 = a2;
                this.attribute_A3 = a3;
                this.attribute_A4 = a4;
                this.attribute_A5 = a5;
                this.attribute_A6 = a6;
                this.attribute_A7 = a7;
                this.attribute_A8 = a8;
                this.attribute_A9 = a9;
                this.bObject = bObj;
        }

        TestClassA(byte a1, short a2, int a3, long a4,
            float a5, double a6, boolean a7, char a8, int[] a9,
            int[][] a10, TestClassB bObj) {
                this.attribute_A1 = a1;
                this.attribute_A2 = a2;
                this.attribute_A3 = a3;
                this.attribute_A4 = a4;
                this.attribute_A5 = a5;
                this.attribute_A6 = a6;
                this.attribute_A7 = a7;
                this.attribute_A8 = a8;
                this.attribute_A9 = a9;
                this.attribute_A10 = a10;
                this.bObject = bObj;
        }

        TestClassA(byte a1, short a2, int a3, long a4,
            float a5, double a6, boolean a7, char a8, int[] a9,
            int[][] a10, TestClassB[] a11, TestClassB bObj) {
                this.attribute_A1 = a1;
                this.attribute_A2 = a2;
                this.attribute_A3 = a3;
                this.attribute_A4 = a4;
                this.attribute_A5 = a5;
                this.attribute_A6 = a6;
                this.attribute_A7 = a7;
                this.attribute_A8 = a8;
                this.attribute_A9 = a9;
                this.attribute_A10 = a10;
                this.attribute_A11 = a11;
                this.bObject = bObj;
        }

        @Override
        public void method_A1(int param1) {
            System.out.println("this is method A1");
        }

        protected void method_B1(int param1) {
            System.out.println("this is method B1");
        }

        private void method_C1() {
            System.out.println("this is method C1");
        }
}
