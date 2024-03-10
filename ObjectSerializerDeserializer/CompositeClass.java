import java.util.Objects;

public class CompositeClass {
    private int attribute_a1;
    private float attribute_a2;
    private char attribute_a3;

    private CompositeClass object_a1;

    CompositeClass() {/*DEFAULT CONSTRUCTOR*/}

    CompositeClass(int a1, float a2, char a3) {
        attribute_a1 = a1;
        attribute_a2 = a2;
        attribute_a3 = a3;
        object_a1 = null;
    }

    CompositeClass(int a1, float a2, char a3, CompositeClass oa1) {
        attribute_a1 = a1;
        attribute_a2 = a2;
        attribute_a3 = a3;
        object_a1 = oa1;
    }

    public int get_attribute_a1() {
        return this.attribute_a1;
    }
    public float get_attribute_a2() {
        return this.attribute_a2;
    }
    public char get_attribute_a3() {
        return this.attribute_a3;
    }
    public CompositeClass get_object_a1() {
        return this.object_a1;
    }

    public void set_attribute_a1(int val) {
        this.attribute_a1 = val;
    }
    public void set_attribute_a2(float val) {
        this.attribute_a2 = val;
    }
    public void set_attribute_a3(char val) {
        this.attribute_a3 = val;
    }
    public void set_object_a1(CompositeClass cco) {
        this.object_a1 = cco;
    }

    @Override
    public String toString() {
        String str = "";
        str += "Class: CompositeClass\n";
        str += "instance: " + hashCode() + "\n";
        str += "\t attribute_a1 = " + this.attribute_a1 + "\n";
        str += "\t attribute_a2 = " + this.attribute_a2 + "\n";
        str += "\t attribute_a3 = " + this.attribute_a3 + "\n";
        if (!Objects.isNull(this.object_a1)) {
            str += "\t object_a1 = \n[" + this.object_a1.toString() + "]\n";
        } else {
            str += "\t object_a1 = null\n";
        }
        
        return str;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CompositeClass)) {
            return false;
        }

        CompositeClass other = (CompositeClass) obj;

        if (!Objects.isNull(this.object_a1)) {
            return (this.attribute_a1 == other.attribute_a1 &&
                this.attribute_a2 == other.attribute_a2 &&
                this.attribute_a3 == other.attribute_a3 &&
                this.object_a1.equals(other.object_a1));
        }

        return (this.attribute_a1 == other.attribute_a1 &&
                this.attribute_a2 == other.attribute_a2 &&
                this.attribute_a3 == other.attribute_a3);
    }
}
