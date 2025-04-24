/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Trans3D.com;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.io.File;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector3d;

/**
 *
 * @author Jesus Alejandro Reyna Chavira 19380510 TECNM Cd.Victoria
 */
public class Menu extends javax.swing.JFrame {

    /**
     * Creates new form Menu
     */
    private float TX = 0.02f;
    private float TY = 0.01f;
    private float TZ = -0.2f;

    private double SX = 1.0f;
    private double SY = 1.0f;
    private double SZ = 1.0f;
    private double SU = 1.0f;

    float xPos = 0;
    float yPos = 0;
    float zPos = 0;
    Transform3D transf = new Transform3D();

    TransformGroup GT = new TransformGroup(transf);
    TransformGroup mouseGrupo = new TransformGroup();
    Vector3f vector = new Vector3f(TX, TY, TZ);
    Vector3d vectorS = new Vector3d(SX, SY, SZ);

    public class R_3D extends javax.swing.JPanel {

        public R_3D() {
            this.setSize(500, 500);

            BtnXP.setVisible(false);
            BtnXN.setVisible(false);
            btnYP.setVisible(false);
            btnYN.setVisible(false);
            btnZP.setVisible(false);
            btnZN.setVisible(false);
            TxtX.setVisible(false);
            TxtY.setVisible(false);
            TxtZ.setVisible(false);
            TxtU.setVisible(false);
            TxtYPA.setVisible(false);
            TxtZPA.setVisible(false);
            btnUP.setVisible(false);
            btnUN.setVisible(false);
            PanelExtras.setVisible(false);

            GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
            Canvas3D canvas3D = new Canvas3D(config);

            setLayout(new BorderLayout());
            add(canvas3D);

            SimpleUniverse universo = new SimpleUniverse(canvas3D);
            universo.getViewingPlatform().setNominalViewingTransform();

            BranchGroup escena = crearGrafoEscena();
            escena.compile();

            universo.addBranchGraph(escena);
        }

        public BranchGroup crearGrafoEscena() {
            BranchGroup objetoRaiz = new BranchGroup();

            Background fondo = new Background(new Color3f(PanelFondo.getBackground()));
            fondo.setApplicationBounds(new BoundingSphere());
            objetoRaiz.addChild(fondo);

            //AparienciaGris
            Appearance appearanceNegro = new Appearance();
            ColoringAttributes coloringAttributesNegro = new ColoringAttributes();
            coloringAttributesNegro.setColor(new Color3f(Color.GRAY));
            appearanceNegro.setColoringAttributes(coloringAttributesNegro);
            //Luz 
            // Ingresa luz natural
            BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

            Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
            ambientLightNode.setInfluencingBounds(bounds);
            objetoRaiz.addChild(ambientLightNode);

            // Ingresa la direccion de la luz
            Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
            Vector3f light1Direction = new Vector3f(0.0f, -0.2f, -1.0f);

            DirectionalLight light1 = new DirectionalLight(light1Color,
                    light1Direction);
            light1.setInfluencingBounds(bounds);
            //----------

            mouseGrupo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            mouseGrupo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

            objetoRaiz.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objetoRaiz.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

            GT.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            GT.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

            objetoRaiz.addChild(mouseGrupo);

            MouseRotate mr = new MouseRotate();
            mr.setTransformGroup(mouseGrupo);
            mr.setSchedulingBounds(new BoundingSphere(new Point3d(), 20000f));
            objetoRaiz.addChild(mr);
            //Cubo
            //X Y Z

            LineArray lineX = new LineArray(2, LineArray.COORDINATES);
            lineX.setCoordinate(0, new Point3f(-1.0f, 0.0f, 0.0f));
            lineX.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f));
            Shape3D shapeLineX = new Shape3D(lineX, appearanceNegro);

            LineArray lineY = new LineArray(2, LineArray.COORDINATES);
            lineY.setCoordinate(0, new Point3f(0.0f, -1.0f, 0.0f));
            lineY.setCoordinate(1, new Point3f(0.0f, 1.0f, 0.0f));
            Shape3D shapeLineY = new Shape3D(lineY, appearanceNegro);

            LineArray lineZ = new LineArray(2, LineArray.COORDINATES);
            lineZ.setCoordinate(0, new Point3f(0.0f, 0.0f, -1.0f));
            lineZ.setCoordinate(1, new Point3f(0.0f, 0.0f, 1.0f));
            Shape3D shapeLineZ = new Shape3D(lineZ, appearanceNegro);

            //Textos 2D
            TransformGroup textTranslationGroup;
            Transform3D textTranslation;
            //X+

            Text2D textoX = new Text2D("X+", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(0.7f, 0f, 0f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoX);

            mouseGrupo.addChild(textTranslationGroup);

            //X-
            Text2D textoX_ = new Text2D("X-", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(-0.8f, 0f, 0f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoX_);

            mouseGrupo.addChild(textTranslationGroup);

            //Y+
            Text2D textoY = new Text2D("Y+", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(0f, 0.8f, 0f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoY);

            mouseGrupo.addChild(textTranslationGroup);

            //Y-
            Text2D textoY_ = new Text2D("Y-", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(0f, -0.8f, 0f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoY_);

            mouseGrupo.addChild(textTranslationGroup);

            //Z+
            Text2D textoZ = new Text2D("Z+", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(0f, 0f, 0.8f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoZ);

            mouseGrupo.addChild(textTranslationGroup);

            //Z-
            Text2D textoZ_ = new Text2D("Z-", new Color3f(Color.white), "Helvetica", 11, Font.BOLD);

            textTranslation = new Transform3D();
            textTranslation.setTranslation(new Vector3f(0f, 0f, -0.8f));
            textTranslationGroup = new TransformGroup(textTranslation);
            textTranslationGroup.addChild(textoZ_);

            mouseGrupo.addChild(textTranslationGroup);
            //RESIZE lo convierte en más grande

            ObjectFile loader = new ObjectFile(ObjectFile.STRIPIFY);
            Scene s = null;

            File file = new java.io.File("model/RMin_OBJ.obj");

            try {
                s = loader.load(file.toURI().toURL());
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }

            //Transformación de la figura//
            //Transform3D traslacion = new Transform3D();
            transf.setTranslation(vector);
            GT.setTransform(transf);
            GT.addChild(s.getSceneGroup());
            mouseGrupo.addChild(GT);
            objetoRaiz.addChild(light1);
            //------------------
            mouseGrupo.addChild(shapeLineX);
            mouseGrupo.addChild(shapeLineY);
            mouseGrupo.addChild(shapeLineZ);

            return objetoRaiz;
        }

    }
    int xMouse, yMouse;

    public Menu() {

        super("Transformaciónes 3D v1.0");
        System.setProperty("sun.awt.noerasebackground", "true");

        initComponents();

        this.setLocationRelativeTo(null);
        R_3D img = new R_3D();
        Panel3D.removeAll();
        Panel3D.add(img);
        Panel3D.repaint();
        Panel3D.revalidate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFondo = new javax.swing.JPanel();
        Header = new javax.swing.JPanel();
        btnSalir = new javax.swing.JPanel();
        IconSalir = new javax.swing.JLabel();
        btnMinim = new javax.swing.JPanel();
        IconMinim = new javax.swing.JLabel();
        btnInfo = new javax.swing.JPanel();
        IconInfo = new javax.swing.JLabel();
        Panel3D = new javax.swing.JPanel();
        PanelObj = new javax.swing.JPanel();
        TxtTitulo = new javax.swing.JLabel();
        Seleccion = new javax.swing.JComboBox<>();
        TxtX = new javax.swing.JLabel();
        TxtY = new javax.swing.JLabel();
        TxtZ = new javax.swing.JLabel();
        TxtU = new javax.swing.JLabel();
        BtnXN = new javax.swing.JPanel();
        jLabelXN = new javax.swing.JLabel();
        BtnXP = new javax.swing.JPanel();
        jLabelXP = new javax.swing.JLabel();
        btnYP = new javax.swing.JPanel();
        jLabelYP = new javax.swing.JLabel();
        btnYN = new javax.swing.JPanel();
        jLabelYN = new javax.swing.JLabel();
        btnZP = new javax.swing.JPanel();
        jLabelZP = new javax.swing.JLabel();
        btnZN = new javax.swing.JPanel();
        jLabelZN = new javax.swing.JLabel();
        PanelExtras = new javax.swing.JPanel();
        btnUP = new javax.swing.JPanel();
        jLabelUP = new javax.swing.JLabel();
        btnUN = new javax.swing.JPanel();
        jLabelUN = new javax.swing.JLabel();
        btnUP1 = new javax.swing.JPanel();
        jLabelUP1 = new javax.swing.JLabel();
        btnUN1 = new javax.swing.JPanel();
        jLabelUN1 = new javax.swing.JLabel();
        btnUN2 = new javax.swing.JPanel();
        jLabelUN2 = new javax.swing.JLabel();
        btnUP2 = new javax.swing.JPanel();
        jLabelUP2 = new javax.swing.JLabel();
        TxtYPA = new javax.swing.JLabel();
        TxtZPA = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFondo.setBackground(new java.awt.Color(86, 200, 216));
        PanelFondo.setBorder(null);
        PanelFondo.setPreferredSize(new java.awt.Dimension(500, 500));
        PanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Header.setBackground(new java.awt.Color(0, 105, 120));
        Header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                HeaderMouseDragged(evt);
            }
        });
        Header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                HeaderMouseReleased(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(0, 105, 120));
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new java.awt.Dimension(47, 47));

        IconSalir.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        IconSalir.setForeground(new java.awt.Color(255, 255, 255));
        IconSalir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IconSalir.setText("X");
        IconSalir.setBorder(null);
        IconSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IconSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        IconSalir.setPreferredSize(new java.awt.Dimension(47, 47));
        IconSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IconSalirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IconSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IconSalirMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnSalirLayout = new javax.swing.GroupLayout(btnSalir);
        btnSalir.setLayout(btnSalirLayout);
        btnSalirLayout.setHorizontalGroup(
            btnSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnSalirLayout.createSequentialGroup()
                .addComponent(IconSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnSalirLayout.setVerticalGroup(
            btnSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IconSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        btnMinim.setBackground(new java.awt.Color(0, 105, 120));
        btnMinim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        IconMinim.setBackground(new java.awt.Color(0, 105, 120));
        IconMinim.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        IconMinim.setForeground(new java.awt.Color(255, 255, 255));
        IconMinim.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IconMinim.setText("—");
        IconMinim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IconMinimMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IconMinimMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IconMinimMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnMinimLayout = new javax.swing.GroupLayout(btnMinim);
        btnMinim.setLayout(btnMinimLayout);
        btnMinimLayout.setHorizontalGroup(
            btnMinimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnMinimLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(IconMinim, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnMinimLayout.setVerticalGroup(
            btnMinimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IconMinim, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        btnInfo.setBackground(new java.awt.Color(0, 105, 120));
        btnInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        IconInfo.setBackground(new java.awt.Color(0, 105, 120));
        IconInfo.setFont(new java.awt.Font("Roboto Black", 0, 18)); // NOI18N
        IconInfo.setForeground(new java.awt.Color(255, 255, 255));
        IconInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IconInfo.setText("I");
        IconInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IconInfoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IconInfoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                IconInfoMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnInfoLayout = new javax.swing.GroupLayout(btnInfo);
        btnInfo.setLayout(btnInfoLayout);
        btnInfoLayout.setHorizontalGroup(
            btnInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IconInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
        );
        btnInfoLayout.setVerticalGroup(
            btnInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IconInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 641, Short.MAX_VALUE)
                .addComponent(btnMinim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        HeaderLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnMinim, btnSalir});

        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMinim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HeaderLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnMinim, btnSalir});

        PanelFondo.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        Panel3D.setBorder(null);
        Panel3D.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout Panel3DLayout = new javax.swing.GroupLayout(Panel3D);
        Panel3D.setLayout(Panel3DLayout);
        Panel3DLayout.setHorizontalGroup(
            Panel3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        Panel3DLayout.setVerticalGroup(
            Panel3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        PanelFondo.add(Panel3D, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        PanelObj.setBackground(new java.awt.Color(0, 151, 167));

        TxtTitulo.setBackground(new java.awt.Color(255, 255, 255));
        TxtTitulo.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        TxtTitulo.setForeground(new java.awt.Color(255, 255, 255));
        TxtTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtTitulo.setText("Transformaciónes 3D");
        TxtTitulo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        Seleccion.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        Seleccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona una Transformación:", "Traslación", "Escalación", "Rotación", "Rotación pivote arbitrario" }));
        Seleccion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Seleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeleccionActionPerformed(evt);
            }
        });

        TxtX.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtX.setForeground(new java.awt.Color(255, 255, 255));
        TxtX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtX.setText("X");
        TxtX.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        TxtY.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtY.setForeground(new java.awt.Color(255, 255, 255));
        TxtY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtY.setText("Y");
        TxtY.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        TxtZ.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtZ.setForeground(new java.awt.Color(255, 255, 255));
        TxtZ.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtZ.setText("Z");
        TxtZ.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        TxtU.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtU.setForeground(new java.awt.Color(255, 255, 255));
        TxtU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtU.setText("X");
        TxtU.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        BtnXN.setBackground(new java.awt.Color(0, 105, 120));

        jLabelXN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelXN.setText("-");
        jLabelXN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelXNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout BtnXNLayout = new javax.swing.GroupLayout(BtnXN);
        BtnXN.setLayout(BtnXNLayout);
        BtnXNLayout.setHorizontalGroup(
            BtnXNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BtnXNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelXN, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        BtnXNLayout.setVerticalGroup(
            BtnXNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BtnXNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelXN, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        BtnXP.setBackground(new java.awt.Color(0, 105, 120));

        jLabelXP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelXP.setText("+");
        jLabelXP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelXPMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout BtnXPLayout = new javax.swing.GroupLayout(BtnXP);
        BtnXP.setLayout(BtnXPLayout);
        BtnXPLayout.setHorizontalGroup(
            BtnXPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BtnXPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelXP, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        BtnXPLayout.setVerticalGroup(
            BtnXPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BtnXPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelXP, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnYP.setBackground(new java.awt.Color(0, 105, 120));

        jLabelYP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelYP.setText("+");
        jLabelYP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelYPMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnYPLayout = new javax.swing.GroupLayout(btnYP);
        btnYP.setLayout(btnYPLayout);
        btnYPLayout.setHorizontalGroup(
            btnYPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnYPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelYP, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnYPLayout.setVerticalGroup(
            btnYPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnYPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelYP, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnYN.setBackground(new java.awt.Color(0, 105, 120));

        jLabelYN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelYN.setText("-");
        jLabelYN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelYNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnYNLayout = new javax.swing.GroupLayout(btnYN);
        btnYN.setLayout(btnYNLayout);
        btnYNLayout.setHorizontalGroup(
            btnYNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnYNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelYN, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnYNLayout.setVerticalGroup(
            btnYNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnYNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelYN, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnZP.setBackground(new java.awt.Color(0, 105, 120));

        jLabelZP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelZP.setText("+");
        jLabelZP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelZPMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnZPLayout = new javax.swing.GroupLayout(btnZP);
        btnZP.setLayout(btnZPLayout);
        btnZPLayout.setHorizontalGroup(
            btnZPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnZPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelZP, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnZPLayout.setVerticalGroup(
            btnZPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnZPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelZP, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnZN.setBackground(new java.awt.Color(0, 105, 120));

        jLabelZN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelZN.setText("-");
        jLabelZN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelZNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnZNLayout = new javax.swing.GroupLayout(btnZN);
        btnZN.setLayout(btnZNLayout);
        btnZNLayout.setHorizontalGroup(
            btnZNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnZNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelZN, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnZNLayout.setVerticalGroup(
            btnZNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnZNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelZN, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelExtras.setBackground(new java.awt.Color(0, 151, 167));
        PanelExtras.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        btnUP.setBackground(new java.awt.Color(0, 105, 120));
        btnUP.setBorder(null);

        jLabelUP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUP.setText("+");
        jLabelUP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUPMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUPLayout = new javax.swing.GroupLayout(btnUP);
        btnUP.setLayout(btnUPLayout);
        btnUPLayout.setHorizontalGroup(
            btnUPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUPLayout.setVerticalGroup(
            btnUPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnUN.setBackground(new java.awt.Color(0, 105, 120));
        btnUN.setBorder(null);

        jLabelUN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUN.setText("-");
        jLabelUN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUNMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUNLayout = new javax.swing.GroupLayout(btnUN);
        btnUN.setLayout(btnUNLayout);
        btnUNLayout.setHorizontalGroup(
            btnUNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUNLayout.setVerticalGroup(
            btnUNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnUP1.setBackground(new java.awt.Color(0, 105, 120));
        btnUP1.setBorder(null);

        jLabelUP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUP1.setText("+");
        jLabelUP1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUP1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUP1Layout = new javax.swing.GroupLayout(btnUP1);
        btnUP1.setLayout(btnUP1Layout);
        btnUP1Layout.setHorizontalGroup(
            btnUP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUP1Layout.setVerticalGroup(
            btnUP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnUN1.setBackground(new java.awt.Color(0, 105, 120));
        btnUN1.setBorder(null);

        jLabelUN1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUN1.setText("-");
        jLabelUN1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUN1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUN1Layout = new javax.swing.GroupLayout(btnUN1);
        btnUN1.setLayout(btnUN1Layout);
        btnUN1Layout.setHorizontalGroup(
            btnUN1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUN1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUN1Layout.setVerticalGroup(
            btnUN1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUN1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnUN2.setBackground(new java.awt.Color(0, 105, 120));
        btnUN2.setBorder(null);

        jLabelUN2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUN2.setText("-");
        jLabelUN2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUN2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUN2Layout = new javax.swing.GroupLayout(btnUN2);
        btnUN2.setLayout(btnUN2Layout);
        btnUN2Layout.setHorizontalGroup(
            btnUN2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUN2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN2, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUN2Layout.setVerticalGroup(
            btnUN2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUN2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUN2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnUP2.setBackground(new java.awt.Color(0, 105, 120));
        btnUP2.setBorder(null);

        jLabelUP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUP2.setText("+");
        jLabelUP2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelUP2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnUP2Layout = new javax.swing.GroupLayout(btnUP2);
        btnUP2.setLayout(btnUP2Layout);
        btnUP2Layout.setHorizontalGroup(
            btnUP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUP2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP2, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnUP2Layout.setVerticalGroup(
            btnUP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnUP2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUP2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout PanelExtrasLayout = new javax.swing.GroupLayout(PanelExtras);
        PanelExtras.setLayout(PanelExtrasLayout);
        PanelExtrasLayout.setHorizontalGroup(
            PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelExtrasLayout.createSequentialGroup()
                        .addComponent(btnUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelExtrasLayout.createSequentialGroup()
                        .addComponent(btnUP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelExtrasLayout.createSequentialGroup()
                        .addComponent(btnUP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUN2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelExtrasLayout.setVerticalGroup(
            PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnUN2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        TxtYPA.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtYPA.setForeground(new java.awt.Color(255, 255, 255));
        TxtYPA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtYPA.setText("Y");
        TxtYPA.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        TxtZPA.setFont(new java.awt.Font("Roboto Medium", 0, 14)); // NOI18N
        TxtZPA.setForeground(new java.awt.Color(255, 255, 255));
        TxtZPA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TxtZPA.setText("Z");
        TxtZPA.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout PanelObjLayout = new javax.swing.GroupLayout(PanelObj);
        PanelObj.setLayout(PanelObjLayout);
        PanelObjLayout.setHorizontalGroup(
            PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelObjLayout.createSequentialGroup()
                .addComponent(TxtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
            .addGroup(PanelObjLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtY, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtX, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtZ, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtYPA, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtZPA, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelObjLayout.createSequentialGroup()
                        .addComponent(btnZP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelObjLayout.createSequentialGroup()
                        .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(PanelExtras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Seleccion, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(PanelObjLayout.createSequentialGroup()
                                .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnYP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BtnXP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnYN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnXN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnZN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        PanelObjLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TxtX, TxtY, TxtZ});

        PanelObjLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BtnXN, BtnXP, btnYN, btnYP, btnZN, btnZP});

        PanelObjLayout.setVerticalGroup(
            PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelObjLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(TxtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Seleccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelObjLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(TxtX)
                        .addGap(40, 40, 40)
                        .addComponent(TxtY)
                        .addGap(42, 42, 42)
                        .addComponent(TxtZ)
                        .addGap(43, 43, 43)
                        .addComponent(TxtU)
                        .addGap(43, 43, 43)
                        .addComponent(TxtYPA)
                        .addGap(43, 43, 43)
                        .addComponent(TxtZPA))
                    .addGroup(PanelObjLayout.createSequentialGroup()
                        .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PanelObjLayout.createSequentialGroup()
                                .addComponent(BtnXN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnYN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelObjLayout.createSequentialGroup()
                                .addComponent(BtnXP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(btnYP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(PanelObjLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnZN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnZP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PanelExtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelObjLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {TxtX, TxtY, TxtZ});

        PanelObjLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BtnXN, BtnXP, btnYN, btnYP, btnZN, btnZP});

        PanelFondo.add(PanelObj, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 310, 560));

        getContentPane().add(PanelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -2, 830, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HeaderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMouseReleased
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_HeaderMouseReleased

    private void HeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_HeaderMouseDragged

    private void IconSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconSalirMouseEntered
        btnSalir.setBackground(Color.red);
        btnSalir.setForeground(Color.white);
    }//GEN-LAST:event_IconSalirMouseEntered

    private void IconSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconSalirMouseExited
        btnSalir.setBackground(Header.getBackground());
        btnSalir.setForeground(Color.black);
    }//GEN-LAST:event_IconSalirMouseExited

    private void IconMinimMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconMinimMouseEntered
        btnMinim.setBackground(Color.red);
        btnMinim.setForeground(Color.white);
    }//GEN-LAST:event_IconMinimMouseEntered

    private void IconMinimMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconMinimMouseExited
        btnMinim.setBackground(Header.getBackground());
        btnMinim.setForeground(Color.black);
    }//GEN-LAST:event_IconMinimMouseExited

    private void IconSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconSalirMouseClicked
        System.exit(0);
    }//GEN-LAST:event_IconSalirMouseClicked

    private void IconMinimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconMinimMouseClicked
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_IconMinimMouseClicked

    private void IconInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconInfoMouseClicked
        Info Info = new Info();
        Info.setVisible(true);
    }//GEN-LAST:event_IconInfoMouseClicked

    private void IconInfoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconInfoMouseEntered
        btnInfo.setBackground(Color.red);
        btnInfo.setForeground(Color.white);
    }//GEN-LAST:event_IconInfoMouseEntered

    private void IconInfoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IconInfoMouseExited
        btnInfo.setBackground(Header.getBackground());
        btnInfo.setForeground(Color.black);
    }//GEN-LAST:event_IconInfoMouseExited

    private void SeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeleccionActionPerformed
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        /* Selecciona una Transformación:
        Traslación
        Escalación
        Rotación*/
        if (Seleccionar.equals("Selecciona una Transformación")) {
            BtnXP.setVisible(false);
            BtnXN.setVisible(false);
            btnYP.setVisible(false);
            btnYN.setVisible(false);
            btnZP.setVisible(false);
            btnZN.setVisible(false);
            TxtX.setVisible(false);
            TxtY.setVisible(false);
            TxtZ.setVisible(false);
            TxtU.setVisible(false);
            btnUP.setVisible(false);
            btnUN.setVisible(false);
        }
        if (Seleccionar.equals("Traslación")) {
            BtnXP.setVisible(true);
            BtnXN.setVisible(true);
            btnYP.setVisible(true);
            btnYN.setVisible(true);
            btnZP.setVisible(true);
            btnZN.setVisible(true);

            TxtX.setVisible(true);
            TxtY.setVisible(true);
            TxtZ.setVisible(true);
            TxtU.setVisible(false);
            btnUP.setVisible(false);
            btnUN.setVisible(false);

        }
        if (Seleccionar.equals("Escalación")) {
            BtnXP.setVisible(true);
            BtnXN.setVisible(true);
            btnYP.setVisible(true);
            btnYN.setVisible(true);
            btnZP.setVisible(true);
            btnZN.setVisible(true);

            btnUP1.setVisible(false);
            btnUP2.setVisible(false);
            btnUN1.setVisible(false);
            btnUN2.setVisible(false);
            TxtX.setVisible(true);
            TxtY.setVisible(true);
            TxtZ.setVisible(true);

            PanelExtras.setVisible(true);
            TxtU.setVisible(true);
            TxtU.setText("U");
            btnUP.setVisible(true);
            btnUN.setVisible(true);

        }
        if (Seleccionar.equals("Rotación")) {
            BtnXP.setVisible(true);
            BtnXN.setVisible(true);
            btnYP.setVisible(true);
            btnYN.setVisible(true);
            btnZP.setVisible(true);
            btnZN.setVisible(true);
            btnUP.setVisible(false);
            btnUN.setVisible(false);
            TxtX.setVisible(true);
            TxtY.setVisible(true);
            TxtZ.setVisible(true);
            TxtU.setVisible(false);

        }
        if (Seleccionar.equals("Rotación pivote arbitrario")) {
           BtnXP.setVisible(true);
            BtnXN.setVisible(true);
            btnYP.setVisible(true);
            btnYN.setVisible(true);
            btnZP.setVisible(true);
            btnZN.setVisible(true);
            btnUP.setVisible(false);
            btnUN.setVisible(false);
            TxtX.setVisible(true);
            TxtY.setVisible(true);
            TxtZ.setVisible(true);
            
            PanelExtras.setVisible(true);
            TxtU.setVisible(true);
            TxtU.setText("X");
            TxtYPA.setVisible(true);
            TxtZPA.setVisible(true);
            btnUP.setVisible(true);
            btnUN.setVisible(true);
            PanelExtras.setVisible(true);

        }
    }//GEN-LAST:event_SeleccionActionPerformed

    private void jLabelXPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelXPMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            xPos += 0.1;
            vector.setX(xPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TX = (float) (TX + 0.1);
            transf.rotX(TX);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SX += 1;
            vectorS.setX(SX);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }
    }//GEN-LAST:event_jLabelXPMouseClicked

    private void jLabelXNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelXNMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            xPos -= 0.1;
            vector.setX(xPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TX = (float) (TX - 0.1);
            transf.rotX(TX);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SX -= 1;
            vectorS.setX(SX);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }
    }//GEN-LAST:event_jLabelXNMouseClicked

    private void jLabelYPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelYPMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            yPos += 0.1;
            vector.setY(yPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TY = (float) (TY + 0.1);
            transf.rotY(TY);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SY += 1;
            vectorS.setY(SY);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }

    }//GEN-LAST:event_jLabelYPMouseClicked

    private void jLabelYNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelYNMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            yPos -= 0.1;
            vector.setY(yPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TY = (float) (TY - 0.1);
            transf.rotY(TY);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SY -= 1;
            vectorS.setY(SY);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }
    }//GEN-LAST:event_jLabelYNMouseClicked

    private void jLabelZPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelZPMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            zPos += 0.1;
            vector.setZ(zPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TZ = (float) (TZ + 0.1);
            transf.rotZ(TZ);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SZ += 1;
            vectorS.setZ(SZ);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }
    }//GEN-LAST:event_jLabelZPMouseClicked

    private void jLabelZNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelZNMouseClicked
        // TODO add your handling code here:
        String Seleccionar = (String) Seleccion.getSelectedItem();
        if (Seleccionar.equals("Traslación")) {
            zPos -= 0.1;
            vector.setZ(zPos);
            transf.setTranslation(vector);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Rotación")) {
            TZ = (float) (TZ - 0.1);
            transf.rotZ(TZ);
            GT.setTransform(transf);
        }
        if (Seleccionar.equals("Escalación")) {
            SZ -= 1;
            vectorS.setZ(SZ);
            transf.setScale(vectorS);

            GT.setTransform(transf);
        }
    }//GEN-LAST:event_jLabelZNMouseClicked

    private void jLabelUPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUPMouseClicked
        // TODO add your handling code here:
        SU += 1;
        transf.setScale(SU);
        // transf.setScale(new Vector3f(SX,SY,SZ));
        GT.setTransform(transf);
    }//GEN-LAST:event_jLabelUPMouseClicked

    private void jLabelUNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUNMouseClicked
        // TODO add your handling code here:
        SU -= 1;
        transf.setScale(SU);
        // transf.setScale(new Vector3f(SX,SY,SZ));
        GT.setTransform(transf);
    }//GEN-LAST:event_jLabelUNMouseClicked

    private void jLabelUP1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUP1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelUP1MouseClicked

    private void jLabelUN1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUN1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelUN1MouseClicked

    private void jLabelUN2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUN2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelUN2MouseClicked

    private void jLabelUP2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelUP2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelUP2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BtnXN;
    private javax.swing.JPanel BtnXP;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel IconInfo;
    private javax.swing.JLabel IconMinim;
    public javax.swing.JLabel IconSalir;
    public javax.swing.JPanel Panel3D;
    private javax.swing.JPanel PanelExtras;
    private javax.swing.JPanel PanelFondo;
    private javax.swing.JPanel PanelObj;
    private javax.swing.JComboBox<String> Seleccion;
    private javax.swing.JLabel TxtTitulo;
    private javax.swing.JLabel TxtU;
    private javax.swing.JLabel TxtX;
    private javax.swing.JLabel TxtY;
    private javax.swing.JLabel TxtYPA;
    private javax.swing.JLabel TxtZ;
    private javax.swing.JLabel TxtZPA;
    private javax.swing.JPanel btnInfo;
    private javax.swing.JPanel btnMinim;
    private javax.swing.JPanel btnSalir;
    private javax.swing.JPanel btnUN;
    private javax.swing.JPanel btnUN1;
    private javax.swing.JPanel btnUN2;
    private javax.swing.JPanel btnUP;
    private javax.swing.JPanel btnUP1;
    private javax.swing.JPanel btnUP2;
    private javax.swing.JPanel btnYN;
    private javax.swing.JPanel btnYP;
    private javax.swing.JPanel btnZN;
    private javax.swing.JPanel btnZP;
    private javax.swing.JLabel jLabelUN;
    private javax.swing.JLabel jLabelUN1;
    private javax.swing.JLabel jLabelUN2;
    private javax.swing.JLabel jLabelUP;
    private javax.swing.JLabel jLabelUP1;
    private javax.swing.JLabel jLabelUP2;
    private javax.swing.JLabel jLabelXN;
    private javax.swing.JLabel jLabelXP;
    private javax.swing.JLabel jLabelYN;
    private javax.swing.JLabel jLabelYP;
    private javax.swing.JLabel jLabelZN;
    private javax.swing.JLabel jLabelZP;
    // End of variables declaration//GEN-END:variables
}
