import java.util.Arrays;

public class PrimitiveArrayClass {
    private int[] attribute_a1;

    PrimitiveArrayClass() {
        attribute_a1 = new int[10];
    }

    PrimitiveArrayClass(int[] inputArray) {
        attribute_a1 = Arrays.copyOf(inputArray, 10);
    }

    public int[] get_attribute_a1() {
        return this.attribute_a1;
    }

    public void set_attribute_a1(int[] val) {
        this.attribute_a1 = val;
    }

    @Override
    public String toString() {
        String str = "";
        str += "Class: PrimitiveArrayClass\n";
        str += "instance: " + hashCode() + "\n";
        str += "\t attribute_a1 = " + Arrays.toString(this.attribute_a1) + "\n";
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        PrimitiveArrayClass other = (PrimitiveArrayClass)obj;
        if (Arrays.equals(this.attribute_a1, other.get_attribute_a1())) {
            result = true;
        }
        return result;
    }
}
