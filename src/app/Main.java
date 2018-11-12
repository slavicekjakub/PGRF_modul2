package app;

import clip.Clipper;
import fill.ScanLine;
import fill.SeedFill;
import model.BufferedImageHelper;
import model.Point;
import model.Polygon;
import renderer.LineRenderer;
import renderer.PolygonRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main {

    private JPanel panel;
    private BufferedImageHelper img;

    private JRadioButton scanLine;
    private JRadioButton seedFill;
    private JRadioButton seedFillPattern;

    // hint line
    private boolean polygonDragged = false;
    private boolean clipperDragged = false;
    private Point temp;
    private LineRenderer lr;

    // polygon
    private Polygon polygon;
    private PolygonRenderer pr;

    // clipping
    private Clipper cl;
    private Polygon clipper;
    private Polygon clipped;

    // fill
    private SeedFill sf;
    private Point seed;
    private ScanLine sl;
    private boolean fillEnabled = false;

    public Main(int width, int height) {
        initGUI(width, height);

        img = new BufferedImageHelper(width, height, BufferedImage.TYPE_INT_RGB);

        lr = new LineRenderer(img);

        polygon = new Polygon();
        pr = new PolygonRenderer(img);

        // create clipping area
        clipper = new Polygon();
        clipper.add(new Point(200, 200));
        clipper.add(new Point(220, 520));
        clipper.add(new Point(480, 420));
        clipper.add(new Point(450, 180));

        cl = new Clipper(clipper);
        clipped = new Polygon();

        sf = new SeedFill(img);
        sl = new ScanLine(img);
    }

    private void initGUI(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF module 2");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
        control.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Radio Buttons for fillers
        scanLine = new JRadioButton("Scan Line");
        scanLine.setSelected(true);
        seedFill = new JRadioButton("Seed Fill");
        seedFillPattern = new JRadioButton("Seed Fill Pattern");

        ButtonGroup fillers = new ButtonGroup();
        fillers.add(scanLine);
        fillers.add(seedFill);
        fillers.add(seedFillPattern);

        control.add(scanLine);
        control.add(seedFill);
        control.add(seedFillPattern);

        // Button for clipping
        JButton clip = new JButton("Clip");
        clip.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clipped = cl.clip(polygon);
                draw();
            }
        });
        control.add(clip);

        frame.add(panel, BorderLayout.WEST);
        frame.add(control, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);

        // for pressing
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // LMB - polygon
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // first press goes to polygon
                    if (polygon.size() < 1)
                        polygon.add(new Point(e.getX(), e.getY()));
                    // MMB - set seed and fill
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    seed = new Point(e.getX(), e.getY());
                    fillEnabled = true;
                }

                draw();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // LMB - polygon points
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // stop dragging
                    if (polygonDragged)
                        polygonDragged = false;

                    polygon.add(new Point(e.getX(), e.getY()));

                    draw();
                    // RMB - clipping area points
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // stop dragging
                    if (clipperDragged)
                        clipperDragged = false;

                    clipper.add(new Point(e.getX(), e.getY()));

                    draw();
                }
            }
        });

        // for dragging
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // LMB - polygon
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // start dragging
                    if (!polygonDragged)
                        polygonDragged = true;
                    // RMB - clipping area
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (!clipperDragged)
                        clipperDragged = true;
                } else if (SwingUtilities.isMiddleMouseButton(e)){
                    // prevent dragging with MMB - removing fill
                    return;
                }

                // hint point
                temp = new Point(e.getX(), e.getY());

                draw();
            }
        });
    }

    private void draw() {
        clear();

        // for polygon - red
        if (polygonDragged) {
            lr.drawDDA(polygon.getAt(0), temp, 0xff0000);
            if (polygon.size() >= 2)
                lr.drawDDA(polygon.getAt(polygon.size() - 1), temp, 0xff0000);
            // for clipper - white
        } else if (clipperDragged) {
            lr.drawDDA(clipper.getAt(0), temp, 0xffffff);
            if (clipper.size() >= 2)
                lr.drawDDA(clipper.getAt(clipper.size() - 1), temp, 0xffffff);
        }

        // draw clipping area - white
        pr.draw(clipper, true, 0xffffff);

        // draw polygon - yellow
        pr.draw(polygon);

        // draw clipped polygon - green
        pr.draw(clipped, true, 0x00ff00);

        // filling
        if (fillEnabled) {
            if (scanLine.isSelected()) {
                // filling with default color
                // if clipped created -> fill it
                if (clipped.size() > 2)
                    sl.fill(clipped);
                    // else fill default poly
                else if (polygon.size() > 2)
                    sl.fill(polygon);
            } else if (seedFill.isSelected()) {
                if (seed != null) {
                    // filling with default color
                    sf.fill((int) seed.getX(), (int) seed.getY(), img.getRGB((int) seed.getX(), (int) seed.getY()));
                }
            } else if (seedFillPattern.isSelected()) {
                if (seed != null) {
                    sf.fillPattern((int) seed.getX(), (int) seed.getY(),
                            img.getRGB((int) seed.getX(), (int) seed.getY()));
                }
            }

            fillEnabled = false;
        }

        img.getGraphics().drawString("LMB - polygon, MMB - fill, RMB - clipping area", 5, img.getHeight() - 5);
        panel.repaint();
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main(800, 600).draw());
    }

}