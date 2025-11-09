package byow.Core;

import java.util.ArrayList;
import java.util.List;

// 并查集
public class UnionSet {
    List<Integer> parents;  // 记录父亲结点或者子树大小
    int size;  // 元素的个数

    UnionSet(int sz) {
        parents = new ArrayList<>();
        for (int i = 0; i < sz; i++) {
            parents.add(-1);
        }
        size = sz;
    }

    int findRoot(int cur) {
        if (parents.get(cur) < 0) {
            return cur;
        }
        int root = findRoot(parents.get(cur));
        parents.set(cur, root);  // 路径压缩
        return root;
    }

    boolean isConnected(int a, int b) {
        return findRoot(a) == findRoot(b);
    }

    void connect(int a, int b) {
        int r1 = findRoot(a), r2 = findRoot(b);
        if (r1 != r2) {
            int c1 = -parents.get(r1), c2 = -parents.get(r2);
            if (c1 < c2) {
                parents.set(r1, r2);
                parents.set(r2, -(c1 + c2));
            } else {
                parents.set(r2, r1);
                parents.set(r1, -(c1 + c2));
            }
        }
    }
}
