import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;


public class Main {

    private static String text;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                final MyTreeTable treeTable = new MyTreeTable();
                treeTable.setTreeTableModel(new DefaultTreeTableModel(new DefaultMutableTreeTableNode("root")));
                JButton button = new JButton("Add nodes");
                button.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DefaultTreeTableModel model = (DefaultTreeTableModel)treeTable.getTreeTableModel();
                        DefaultMutableTreeTableNode parentNode = null;
                        TreePath parentPath = treeTable.getTreeSelectionModel().getSelectionPath();
                 
                        if (parentPath == null) {
                            parentNode = (DefaultMutableTreeTableNode) model.getRoot();
                        } else {
                            parentNode = (DefaultMutableTreeTableNode) (parentPath.getLastPathComponent());
                        }
                        model.insertNodeInto(new DefaultMutableTreeTableNode("Aname"+System.currentTimeMillis()), parentNode, parentNode.getChildCount());
                        treeTable.expandAll();
                    }
                });
                JTextField filterText = new JTextField();
                filterText.addCaretListener(new CaretListener(){
                    @Override
                    public void caretUpdate(CaretEvent e) {
                        text = ((JTextField) e.getSource()).getText();
                        if (text.length() > 0) {
                            treeTable.setModelFilter(new TellapicModelFilter(treeTable){
                                @Override
                                public boolean shouldBeFiltered(AbstractMutableTreeTableNode node) {
                                    return node.getUserObject().toString().toLowerCase().startsWith(text.toLowerCase());
                                }
                            });
                        } else {
                            treeTable.setModelFilter(null);
                        }
                    }
                });
                JFrame frame = new JFrame("Filter test");
                frame.getContentPane().add(treeTable, BorderLayout.CENTER);
                frame.getContentPane().add(filterText, BorderLayout.NORTH);
                frame.getContentPane().add(button, BorderLayout.SOUTH);
                frame.setPreferredSize(new Dimension(800, 600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
