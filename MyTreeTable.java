import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;


public class MyTreeTable extends JXTreeTable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TreeTableModel model;
    private TellapicModelFilter modelFilter;

    /*
     * (non-Javadoc)
     * @see org.jdesktop.swingx.JXTreeTable#setTreeTableModel(org.jdesktop.swingx.treetable.TreeTableModel)
     */
    @Override
    public void setTreeTableModel(TreeTableModel treeModel) {
        if (!(treeModel instanceof TellapicModelFilter))
            model = treeModel;

        super.setTreeTableModel(treeModel);
    }
    
    /**
     * 
     * @return
     */
    public TreeTableModel getUnfilteredModel() {
        return model;
    }

    /**
     * @param mf
     */
    public void setModelFilter(TellapicModelFilter mf) {
        if (modelFilter != null) {
            modelFilter.restoreFamily();
            setTreeTableModel(getUnfilteredModel());
        }
        // Is this necessary?
        if (mf == null) {
            setTreeTableModel(getUnfilteredModel());
        } else {
            modelFilter = mf;
            modelFilter.filter();
            setTreeTableModel(modelFilter);
        }
    }
}
