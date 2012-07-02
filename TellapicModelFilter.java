import java.util.HashMap;
import java.util.Map;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;


public abstract class TellapicModelFilter extends DefaultTreeTableModel{
    
    protected Map<AbstractMutableTreeTableNode, AbstractMutableTreeTableNode>  family;
    protected Map<AbstractMutableTreeTableNode, AbstractMutableTreeTableNode>  filter;
    protected MyTreeTable                      treeTable;
    private boolean                            withChildren;
    private boolean                            withParents;
    
    /**
     * 
     * @param model
     */
    public TellapicModelFilter(MyTreeTable treeTable) {
        this(treeTable, false, false);
    }
    
    /**
     * 
     * @param treeTable
     * @param wp
     * @param wc
     */
    public TellapicModelFilter(MyTreeTable treeTable, boolean wp, boolean wc) {
        super(new DefaultMutableTreeTableNode("filteredRoot"));
        this.treeTable = treeTable;
        setIncludeChildren(wc);
        setIncludeParents(wp);
    }
    
    /**
     * 
     */
    public void filter() {
        filter = new HashMap<AbstractMutableTreeTableNode, AbstractMutableTreeTableNode>();
        family = new HashMap<AbstractMutableTreeTableNode, AbstractMutableTreeTableNode>();
        AbstractMutableTreeTableNode filteredRoot = (AbstractMutableTreeTableNode) getRoot();
        AbstractMutableTreeTableNode root = (AbstractMutableTreeTableNode) treeTable.getTreeTableModel().getRoot();
        filterChildren(root, filteredRoot);
        for(AbstractMutableTreeTableNode node : family.keySet())
            node.setParent(null);
        for(AbstractMutableTreeTableNode node : filter.keySet())
            node.setParent(filter.get(node));
    }
    
    /**
     * 
     * @param node
     * @param filteredNode
     */
    private void filterChildren(AbstractMutableTreeTableNode node, AbstractMutableTreeTableNode filteredNode) {
        int count = node.getChildCount();
        for(int i = 0; i < count; i++) {
            AbstractMutableTreeTableNode child = (AbstractMutableTreeTableNode) node.getChildAt(i);
            family.put(child, node);
            if (shouldBeFiltered(child)) {
                filter.put(child, filteredNode);
                if (includeChildren())
                    filterChildren(child, child);
            } else {
                filterChildren(child, filteredNode);
            }
        }
    }
    
    /**
     * 
     */
    public void restoreFamily() {
        for(AbstractMutableTreeTableNode child : family.keySet())
            family.get(child).add(child);
    }
    
    /**
     * 
     * @param node
     * @return
     */
    public abstract boolean shouldBeFiltered(AbstractMutableTreeTableNode node); 
    
    /**
     * Determines if parents will be included in the filtered result. This DOES NOT means that parent will be filtered
     * with the filter criteria. Instead, if a node {@code}shouldBeFiltered{@code} no matter what the parent node is,
     * include it in the filter result. The use of this feature is to provide contextual data about the filtered node,
     * in the terms of: "where was this node that belongs to?"
     * 
     * @return True is parents should be included anyhow.
     */
    public boolean includeParents() {
        return withParents;
    }

    /**
     * Determines if children should be filtered. When a node {@code}shouldBeFiltered{@code} you can stop the filtering
     * process in that node by setting: {@code}setIncludeChildren(false){@code}. In other words, if you want to filter
     * all the tree, {@code}includeChildren{@code} should return true.
     * 
     * By letting this method return {@code}false{@code} all children of the node filtered will be automatically added
     * to the resulting filter. That is, children aren't filtered with the filter criteria and they will be shown with
     * their parent in the filter result.
     * 
     * @return True if you want to filter all the tree.
     */
    public boolean includeChildren() {
        return withChildren;
    }
    
    /**
     * 
     * @param include
     */
    public void setIncludeParents(boolean include) {
        withParents = include;
    }

    /**
     * 
     * @param include
     */
    public void setIncludeChildren(boolean include) {
        withChildren = include;
    }
}
