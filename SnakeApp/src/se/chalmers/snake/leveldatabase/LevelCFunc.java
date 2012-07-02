package se.chalmers.snake.leveldatabase;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

/**
 *
 */
class LevelCFunc {

    String type;
    List<Float> val = new ArrayList<Float>();

    LevelCFunc(Node nodeMeta) throws Exception {
        this.type = XMLReader.attribute(nodeMeta, XMLLevel.XMLString.CFUNC);
        String values = XMLReader.val(nodeMeta);
        if (values == null) {
            throw new IllegalArgumentException("No vaild data in this XML node");
        }
        for (String value : values.split(";")) {
            try {
                this.val.add(Float.parseFloat(value));
            } catch (NumberFormatException ex) {
            }
        }


    }

    private void test(int x) {
        if (this.val.get(x) == null) {
            throw new IllegalArgumentException("No vaild value in the level XML file for it type: " + this.type);
        }
    }

    float calc(int x) {
        this.test(0);
        if ("greater".equals(this.type)) {
            return x >= this.val.get(0) ? 1 : 0;
        }

        if ("lower".equals(this.type)) {
            return x <= this.val.get(0) ? 1 : 0;
        }

        if ("multi".equals(this.type)) {
            this.test(1);
            return this.val.get(0) * x + this.val.get(1);
        }



        return this.val.get(0);
    }

    float calc(List<Integer> list) {
        this.test(0);

        if ("timegoal".equals(this.type)) {
            for (Integer integer : list) {
                if (integer < this.val.get(0)) {
                    return 1;
                }
            }
            return 0;
        }


        return this.calc(list.size());
    }

    float calc(int a, int b) {


        return this.calc(a);
    }

    @Override
    public String toString() {
        return "CFunc{" + "type=" + type + ", val=" + this.val.toString() + '}';
    }
}
