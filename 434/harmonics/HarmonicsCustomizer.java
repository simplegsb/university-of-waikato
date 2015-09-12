package harmonics;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

public class HarmonicsCustomizer extends JPanel implements Customizer
{
    private PropertyChangeSupport pcsupport = new PropertyChangeSupport(this);

    private Harmonics harmonics;

    private JList list;

    private JPanel p;

    public HarmonicsCustomizer()
    {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Select Harmonics:", JLabel.CENTER);
        add(label, BorderLayout.NORTH);
        DefaultListModel dl = new DefaultListModel();
        for (int i = 1; i <= Harmonics.NFREQUENCIES; i++)
        {
            String s = "(1/" + i + ")(sin" + i + "x)";
            dl.addElement(s);
        }
        list = new JList(dl);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                BitSet frequencies = new BitSet(Harmonics.NFREQUENCIES);
                for (int i = 0; i < Harmonics.NFREQUENCIES; i++)
                {
                    if (list.isSelectedIndex(i))
                    {
                        frequencies.set(i);
                    }
                }
                harmonics.setFrequencies(frequencies);
            }
        });

        p = new JPanel();
        p.add(list);
        add(p, BorderLayout.CENTER);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(300, 380);
    }

    public void setObject(Object object)
    {

        // Save reference to the Harmonics object
        harmonics = (Harmonics) object;

        // Get data from the harmonics object
        BitSet frequencies = harmonics.getFrequencies();
        for (int i = 0; i < Harmonics.NFREQUENCIES; i++)
        {
            if (frequencies.get(i))
            {
                list.addSelectionInterval(i, i);
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        pcsupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        pcsupport.removePropertyChangeListener(pcl);
    }
}
