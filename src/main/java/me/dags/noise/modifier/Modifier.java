package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 *
 * Modifiers alter the output of one or more Modules
 */
public abstract class Modifier implements Module {

    protected final Module source;

    public Modifier(Module source) {
        this.source = source;
    }

    @Override
    public float getValue(float x, float y) {
        return modify(x, y, source.getValue(x, y));
    }

    @Override
    public float minValue() {
        return source.minValue();
    }

    @Override
    public float maxValue() {
        return source.maxValue();
    }

    @Override
    public void toNode(Node node) {
        node.clear();
        node.set("module", getName());
        source.toNode(node.node("source"));
    }

    @Override
    public String toString() {
        return getName() + "{" + source + "}";
    }

    public abstract float modify(float x, float y, float noiseValue);
}
