import java.util.Arrays;

public class CompositeArrayClass {
    private CompositeClass[] attribute_a1;

    CompositeArrayClass() {
        attribute_a1 = new CompositeClass[3];
    }

    CompositeArrayClass(CompositeClass[] inputArray) {
        attribute_a1 = Arrays.copyOf(inputArray, 3);
    }

    public CompositeClass[] get_attribute_a1() {
        return this.attribute_a1;
    }

    public void set_attribute_a1(CompositeClass[] val) {
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

        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof CompositeArrayClass)) {
            return false;
        }

        CompositeArrayClass other = (CompositeArrayClass) obj;

        return (Arrays.equals(this.attribute_a1, ((CompositeArrayClass)obj).attribute_a1));
    }
}
