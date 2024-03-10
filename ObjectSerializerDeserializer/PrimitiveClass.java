public class PrimitiveClass {
    private byte attribute_a1;
    private short attribute_a2;
    private int attribute_a3;
    private long attribute_a4;
    private float attribute_a5;
    private double attribute_a6;
    private boolean attribute_a7;
    private char attribute_a8;

    //DEV NOTE: ask TA about objects with no default constructor
    //(need to be able to get constructor objects with null parameter lists) 
    PrimitiveClass() {/*DEFAULT CONSTRUCTOR*/}

    PrimitiveClass(byte a1, short a2, int a3, long a4, 
        float a5, double a6, boolean a7, char a8) {
            attribute_a1 = a1;
            attribute_a2 = a2;
            attribute_a3 = a3;
            attribute_a4 = a4;
            attribute_a5 = a5;
            attribute_a6 = a6;
            attribute_a7 = a7;
            attribute_a8 = a8;
    }

    public byte get_attribute_a1() {
        return this.attribute_a1;
    }
    public short get_attribute_a2() {
        return this.attribute_a2;
    }
    public int get_attribute_a3() {
        return this.attribute_a3;
    }
    public long get_attribute_a4() {
        return this.attribute_a4;
    }
    public float get_attribute_a5() {
        return this.attribute_a5;
    }
    public double get_attribute_a6() {
        return this.attribute_a6;
    }
    public boolean get_attribute_a7() {
        return this.attribute_a7;
    }
    public char get_attribute_a8() {
        return this.attribute_a8;
    }

    public void set_attribute_a1(byte val) {
        this.attribute_a1 = val;
    } 
    public void set_attribute_a2(short val) {
        this.attribute_a2 = val;
    } 
    public void set_attribute_a3(int val) {
        this.attribute_a3 = val;
    } 
    public void set_attribute_a4(long val) {
        this.attribute_a4 = val;
    } 
    public void set_attribute_a5(float val) {
        this.attribute_a5 = val;
    } 
    public void set_attribute_a6(double val) {
        this.attribute_a6 = val;
    } 
    public void set_attribute_a7(boolean val) {
        this.attribute_a7 = val;
    } 
    public void set_attribute_a8(char val) {
        this.attribute_a8 = val;
    }
    
    @Override
    public String toString() {
        String str = "";
        
        str += "Class: PrimitiveClass\n";
        str += "instance: " + hashCode() + "\n";
        str += "\t attribute_a1 = " + this.attribute_a1 + "\n";
        str += "\t attribute_a2 = " + this.attribute_a2 + "\n";
        str += "\t attribute_a3 = " + this.attribute_a3 + "\n";
        str += "\t attribute_a4 = " + this.attribute_a4 + "\n";
        str += "\t attribute_a5 = " + this.attribute_a5 + "\n";
        str += "\t attribute_a6 = " + this.attribute_a6 + "\n";
        str += "\t attribute_a7 = " + this.attribute_a7 + "\n";
        str += "\t attribute_a8 = " + this.attribute_a8 + "\n";
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        obj = (PrimitiveClass) obj;
        if (this.attribute_a1 == ((PrimitiveClass) obj).get_attribute_a1() &&
            this.attribute_a2 == ((PrimitiveClass) obj).get_attribute_a2() &&
            this.attribute_a3 == ((PrimitiveClass) obj).get_attribute_a3() &&
            this.attribute_a4 == ((PrimitiveClass) obj).get_attribute_a4() &&
            this.attribute_a5 == ((PrimitiveClass) obj).get_attribute_a5() &&
            this.attribute_a6 == ((PrimitiveClass) obj).get_attribute_a6() &&
            this.attribute_a7 == ((PrimitiveClass) obj).get_attribute_a7() &&
            this.attribute_a8 == ((PrimitiveClass) obj).get_attribute_a8()){
                result = true;
        }
        return result;
    }
}
