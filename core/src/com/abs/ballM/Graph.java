package com.abs.ballM;

import com.badlogic.gdx.utils.Array;

/**
 * Created by k on 31.07.15.
 */
class Graph {
    final static int  NOT_VISIT = 0;
    final static int      VISIT = 1;
    final static int LAST_VISIT = 2;

    Array<GraphNode> queue;

    private class GraphNode {
        Array<Integer> link = new Array<Integer>();
        int type = 0;
    }

//    ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
//    ArrayList<Integer> removes = new ArrayList<Integer>();

    Array<GraphNode> nodes = new Array<GraphNode>();
    Array<Integer> removes = new Array<Integer>();
    Array< Array<Integer>> groups = new Array<Array<Integer>>();

    final static int MAX_COUNT = 4;

    public void createGraph(int n){
        groups.clear();
        removes.clear();
        //nodes.clear();
        for(int i = 0; i < n; i++){
            if(i >= nodes.size) {
                nodes.add(new GraphNode());
            }else{
                nodes.get(i).link.clear();
                nodes.get(i).type = 0;
            }
        }

        queue = new Array<GraphNode>();
    }

    public void addEdge(int a, int b){
        nodes.get(a).link.add(b);
        nodes.get(b).link.add(a);
    }

    public void bfs(int startNode){
        int count = 1;
        queue.add(nodes.get(startNode));
        nodes.get(startNode).type = VISIT;
        while(queue.size != 0) {
            GraphNode node = queue.pop();

            for(int i : node.link){
                GraphNode localNode = nodes.get(i);
                if(localNode.type == NOT_VISIT){
                    localNode.type = VISIT;
                    queue.add(localNode);
                    count++;
                }
            }
        }

//        if(count >= MAX_COUNT){
//            for(int i = 0; i < nodes.size; i++){
//                if(nodes.get(i).type == VISIT){
//                    removes.add(i);
//                }
//            }
//        }

        if(count >= MAX_COUNT) {
            Array<Integer> tmp = new Array<Integer>();
            groups.add(tmp);
            for (int i = 0; i < nodes.size; i++) {
                if (nodes.get(i).type == VISIT) {
                    groups.get(groups.size - 1).add(i);
                }
            }
        }

        for(GraphNode node : nodes){
            if(node.type == VISIT){
                node.type = LAST_VISIT;
            }
        }
    }

    public Array<Integer> searchBigGroups(){
        for(int i = 0; i < nodes.size; i++){
            if(nodes.get(i).type == NOT_VISIT){
                bfs(i);
            }
        }

        removes.sort();
        removes.reverse();
        //Collections.sort(removes, Collections.reverseOrder());
        return removes;
    }

    public Array<Array< Integer> > searchGroups(){
        for(int i = 0; i < nodes.size; i++){
            if(nodes.get(i).type == NOT_VISIT){
                bfs(i);
            }
        }

        for(Array<Integer> tmp : groups){
            tmp.sort();
            //tmp.reverse();
        }

        return groups;
    }
}
