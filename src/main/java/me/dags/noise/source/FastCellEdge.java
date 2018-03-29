package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class FastCellEdge extends FastSource {

    private final EdgeFunc edgeFunc;
    private final DistanceFunc distFunc;

    public FastCellEdge(Builder builder) {
        super(builder);
        this.edgeFunc = builder.getEdgeFunc();
        this.distFunc = builder.getDistFunc();
    }

    @Override
    public String getName() {
        return "cell_edge";
    }

    @Override
    public float value(float x, float y) {
        x *= frequency;
        y *= frequency;
        float value = Noise.singleCellular2Edge(x, y, seed, edgeFunc, distFunc);
        return NoiseUtil.map(value, -1, 1, 2);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        Util.setNonDefault(node, "edge", edgeFunc, Builder.EDGE_FUNC);
        Util.setNonDefault(node, "dist", distFunc, Builder.DIST_FUNC);
    }

    @Override
    public String toString() {
        return getName() + "{"
                + properties()
                + ", edge=" + edgeFunc
                + ", dist=" + distFunc
                + "}";
    }
}
