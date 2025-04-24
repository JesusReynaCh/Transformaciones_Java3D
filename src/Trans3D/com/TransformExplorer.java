package Trans3D.com;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Link;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.Screen3D;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Switch;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/*
 *  
 */

public class TransformExplorer extends Applet implements
    Java3DExplorerConstants {

  SimpleUniverse u;

  boolean isApplication;

  Canvas3D canvas;

  OffScreenCanvas3D offScreenCanvas;

  View view;

  TransformGroup coneTG;

  // transformation factors for the cone
  Vector3f coneTranslation = new Vector3f(0.0f, 0.0f, 0.0f);

  float coneScale = 1.0f;

  Vector3d coneNUScale = new Vector3d(1.0f, 1.0f, 1.0f);

  Vector3f coneRotateAxis = new Vector3f(1.0f, 0.0f, 0.0f);

  Vector3f coneRotateNAxis = new Vector3f(1.0f, 0.0f, 0.0f);

  float coneRotateAngle = 0.0f;

  AxisAngle4f coneRotateAxisAngle = new AxisAngle4f(coneRotateAxis,
      coneRotateAngle);

  Vector3f coneRefPt = new Vector3f(0.0f, 0.0f, 0.0f);

  // this tells whether to use the compound transformation
  boolean useCompoundTransform = true;

  // These are Transforms are used for the compound transformation
  Transform3D translateTrans = new Transform3D();

  Transform3D scaleTrans = new Transform3D();

  Transform3D rotateTrans = new Transform3D();

  Transform3D refPtTrans = new Transform3D();

  Transform3D refPtInvTrans = new Transform3D();

  // this tells whether to use the uniform or non-uniform scale when
  // updating the compound transform
  boolean useUniformScale = true;

  // The size of the cone
  float coneRadius = 1.0f;

  float coneHeight = 2.0f;

  // The axis indicator, used to show the rotation axis
  RotAxis rotAxis;

  boolean showRotAxis = false;

  float rotAxisLength = 3.0f;

  // The coord sys used to show the coordinate system
  CoordSys coordSys;

  boolean showCoordSys = true;

  float coordSysLength = 5.0f;

  // GUI elements
  String rotAxisString = "Rotation Axis";

  String coordSysString = "Coord Sys";

  JCheckBox rotAxisCheckBox;

  JCheckBox coordSysCheckBox;

  String snapImageString = "Snap Image";

  String outFileBase = "transform";

  int outFileSeq = 0;

  float offScreenScale;

  JLabel coneRotateNAxisXLabel;

  JLabel coneRotateNAxisYLabel;

  JLabel coneRotateNAxisZLabel;

  // Temporaries that are reused
  Transform3D tmpTrans = new Transform3D();

  Vector3f tmpVector = new Vector3f();

  AxisAngle4f tmpAxisAngle = new AxisAngle4f();

  // geometric constant
  Point3f origin = new Point3f();

  Vector3f yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

  // Returns the TransformGroup we will be editing to change the transform
  // on the cone
  TransformGroup createConeTransformGroup() {

    // create a TransformGroup for the cone, allow tranform changes,
    coneTG = new TransformGroup();
    coneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    coneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

    // Set up an appearance to make the Cone with red ambient,
    // black emmissive, red diffuse and white specular coloring
    Material material = new Material(red, black, red, white, 64);
    // These are the colors used for the book figures:
    //Material material = new Material(white, black, white, black, 64);
    Appearance appearance = new Appearance();
    appearance.setMaterial(material);

    // create the cone and add it to the coneTG
    Cone cone = new Cone(coneRadius, coneHeight, appearance);
    coneTG.addChild(cone);

    return coneTG;
  }

  void setConeTranslation() {
    coneTG.getTransform(tmpTrans); // get the old transform
    tmpTrans.setTranslation(coneTranslation); // set only translation
    coneTG.setTransform(tmpTrans); // set the new transform
  }

  void setConeUScale() {
    coneTG.getTransform(tmpTrans); // get the old transform
    tmpTrans.setScale(coneScale); // set only scale
    coneTG.setTransform(tmpTrans); // set the new transform
  }

  void setConeNUScale() {
    coneTG.getTransform(tmpTrans); // get the old transform
    System.out.println("coneNUScale.x = " + coneNUScale.x);
    tmpTrans.setScale(coneNUScale);// set only scale
    coneTG.setTransform(tmpTrans); // set the new transform
  }

  void setConeRotation() {
    coneTG.getTransform(tmpTrans); // get the old transform
    tmpTrans.setRotation(coneRotateAxisAngle); // set only rotation
    coneTG.setTransform(tmpTrans); // set the new transform
  }

  void updateUsingCompoundTransform() {
    // set the component transformations
    translateTrans.set(coneTranslation);
    if (useUniformScale) {
      scaleTrans.set(coneScale);
    } else {
      scaleTrans.setIdentity();
      scaleTrans.setScale(coneNUScale);
    }
    rotateTrans.set(coneRotateAxisAngle);

    // translate from ref pt to origin
    tmpVector.sub(origin, coneRefPt); // vector from ref pt to origin
    refPtTrans.set(tmpVector);

    // translate from origin to ref pt
    tmpVector.sub(coneRefPt, origin); // vector from origin to ref pt
    refPtInvTrans.set(tmpVector);

    // now build up the transfomation
    // trans = translate * refPtInv * scale * rotate * refPt;
    tmpTrans.set(translateTrans);
    tmpTrans.mul(refPtInvTrans);
    tmpTrans.mul(scaleTrans);
    tmpTrans.mul(rotateTrans);
    tmpTrans.mul(refPtTrans);

    // Copy the transform to the TransformGroup
    coneTG.setTransform(tmpTrans);
  }

  // ensure that the cone rotation axis is a unit vector
  void normalizeConeRotateAxis() {
    // normalize, watch for length == 0, if so, then use default
    float lengthSquared = coneRotateAxis.lengthSquared();
    if (lengthSquared > 0.0001) {
      coneRotateNAxis.scale((float) (1.0 / Math.sqrt(lengthSquared)),
          coneRotateAxis);
    } else {
      coneRotateNAxis.set(1.0f, 0.0f, 0.0f);
    }
  }

  // copy the current axis and angle to the axis angle, convert angle
  // to radians
  void updateConeAxisAngle() {
    coneRotateAxisAngle.set(coneRotateNAxis, (float) Math
        .toRadians(coneRotateAngle));
  }

  void updateConeRotateNormalizedLabels() {
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    coneRotateNAxisXLabel.setText("X: " + nf.format(coneRotateNAxis.x));
    coneRotateNAxisYLabel.setText("Y: " + nf.format(coneRotateNAxis.y));
    coneRotateNAxisZLabel.setText("Z: " + nf.format(coneRotateNAxis.z));
  }

  BranchGroup createSceneGraph() {
    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();

    // Create a TransformGroup to scale the scene down by 3.5x
    TransformGroup objScale = new TransformGroup();
    Transform3D scaleTrans = new Transform3D();
    scaleTrans.set(1 / 3.5f); // scale down by 3.5x
    objScale.setTransform(scaleTrans);
    objRoot.addChild(objScale);

    // Create a TransformGroup and initialize it to the
    // identity. Enable the TRANSFORM_WRITE capability so that
    // the mouse behaviors code can modify it at runtime. Add it to the
    // root of the subgraph.
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objScale.addChild(objTrans);

    // Add the primitives to the scene
    objTrans.addChild(createConeTransformGroup()); // the cone
    rotAxis = new RotAxis(rotAxisLength); // the axis
    objTrans.addChild(rotAxis);
    coordSys = new CoordSys(coordSysLength); // the coordSys
    objTrans.addChild(coordSys);

    BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

    // The book used a white background for the figures
    //Background bg = new Background(new Color3f(1.0f, 1.0f, 1.0f));
    //bg.setApplicationBounds(bounds);
    //objTrans.addChild(bg);

    // Set up the ambient light
    Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
    AmbientLight ambientLightNode = new AmbientLight(ambientColor);
    ambientLightNode.setInfluencingBounds(bounds);
    objRoot.addChild(ambientLightNode);

    // Set up the directional lights
    Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
    Vector3f light1Direction = new Vector3f(0.0f, -0.2f, -1.0f);

    DirectionalLight light1 = new DirectionalLight(light1Color,
        light1Direction);
    light1.setInfluencingBounds(bounds);
    objRoot.addChild(light1);

    return objRoot;
  }

  public TransformExplorer() {
    this(false, 1.0f);
  }

  public TransformExplorer(boolean isApplication, float initOffScreenScale) {
    this.isApplication = isApplication;
    this.offScreenScale = initOffScreenScale;
  }


  public void init() {

    setLayout(new BorderLayout());
    GraphicsConfiguration config = SimpleUniverse
        .getPreferredConfiguration();

    canvas = new Canvas3D(config);
    add("Center", canvas);

    u = new SimpleUniverse(canvas);

    if (isApplication) {
      offScreenCanvas = new OffScreenCanvas3D(config, true);
      // set the size of the off-screen canvas based on a scale
      // of the on-screen size
      Screen3D sOn = canvas.getScreen3D();
      Screen3D sOff = offScreenCanvas.getScreen3D();
      Dimension dim = sOn.getSize();
      dim.width *= offScreenScale;
      dim.height *= offScreenScale;
      sOff.setSize(dim);
      sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
          * offScreenScale);
      sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
          * offScreenScale);

      // attach the offscreen canvas to the view
      u.getViewer().getView().addCanvas3D(offScreenCanvas);
    }

    // Create a simple scene and attach it to the virtual universe
    BranchGroup scene = createSceneGraph();

    // get the view
    view = u.getViewer().getView();

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    ViewingPlatform viewingPlatform = u.getViewingPlatform();
    viewingPlatform.setNominalViewingTransform();

    // add an orbit behavior to move the viewing platform
    OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.STOP_ZOOM);
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);
    orbit.setSchedulingBounds(bounds);
    viewingPlatform.setViewPlatformBehavior(orbit);

    u.addBranchGraph(scene);

    add("East", guiPanel());
  }

  // create a panel with a tabbed pane holding each of the edit panels
  JPanel guiPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Translation", translationPanel());
    tabbedPane.addTab("Scaling", scalePanel());
    tabbedPane.addTab("Rotation", rotationPanel());
    tabbedPane.addTab("Reference Point", refPtPanel());
    panel.add("Center", tabbedPane);
    panel.add("South", configPanel());
    return panel;
  }

  Box translationPanel() {
    Box panel = new Box(BoxLayout.Y_AXIS);

    panel.add(new LeftAlignComponent(new JLabel("Translation Offset")));

    // X translation label, slider, and value label
    FloatLabelJSlider coneTranslateXSlider = new FloatLabelJSlider("X",
        0.1f, -2.0f, 2.0f, coneTranslation.x);
    coneTranslateXSlider.setMajorTickSpacing(1.0f);
    coneTranslateXSlider.setPaintTicks(true);
    coneTranslateXSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneTranslation.x = e.getValue();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeTranslation();
        }
      }
    });
    panel.add(coneTranslateXSlider);

    // Y translation label, slider, and value label
    FloatLabelJSlider coneTranslateYSlider = new FloatLabelJSlider("Y",
        0.1f, -2.0f, 2.0f, coneTranslation.y);
    coneTranslateYSlider.setMajorTickSpacing(1.0f);
    coneTranslateYSlider.setPaintTicks(true);
    coneTranslateYSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneTranslation.y = e.getValue();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeTranslation();
        }
      }
    });
    panel.add(coneTranslateYSlider);

    // Z translation label, slider, and value label
    FloatLabelJSlider coneTranslateZSlider = new FloatLabelJSlider("Z",
        0.1f, -2.0f, 2.0f, coneTranslation.z);
    coneTranslateZSlider.setMajorTickSpacing(1.0f);
    coneTranslateZSlider.setPaintTicks(true);
    coneTranslateZSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneTranslation.z = e.getValue();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeTranslation();
        }
      }
    });
    panel.add(coneTranslateZSlider);

    return panel;
  }

  Box scalePanel() {
    Box panel = new Box(BoxLayout.Y_AXIS);

    // Uniform Scale
    JLabel uniform = new JLabel("Uniform Scale");
    panel.add(new LeftAlignComponent(uniform));

    FloatLabelJSlider coneScaleSlider = new FloatLabelJSlider("S:", 0.1f,
        0.0f, 3.0f, coneScale);
    coneScaleSlider.setMajorTickSpacing(1.0f);
    coneScaleSlider.setPaintTicks(true);
    coneScaleSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneScale = e.getValue();
        useUniformScale = true;
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeUScale();
        }
      }
    });
    panel.add(coneScaleSlider);

    JLabel nonUniform = new JLabel("Non-Uniform Scale");
    panel.add(new LeftAlignComponent(nonUniform));

    // Non-Uniform Scale
    FloatLabelJSlider coneNUScaleXSlider = new FloatLabelJSlider("X: ",
        0.1f, 0.0f, 3.0f, (float) coneNUScale.x);
    coneNUScaleXSlider.setMajorTickSpacing(1.0f);
    coneNUScaleXSlider.setPaintTicks(true);
    coneNUScaleXSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneNUScale.x = (double) e.getValue();
        useUniformScale = false;
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeNUScale();
        }
      }
    });
    panel.add(coneNUScaleXSlider);

    FloatLabelJSlider coneNUScaleYSlider = new FloatLabelJSlider("Y: ",
        0.1f, 0.0f, 3.0f, (float) coneNUScale.y);
    coneNUScaleYSlider.setMajorTickSpacing(1.0f);
    coneNUScaleYSlider.setPaintTicks(true);
    coneNUScaleYSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneNUScale.y = (double) e.getValue();
        useUniformScale = false;
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeNUScale();
        }
      }
    });
    panel.add(coneNUScaleYSlider);

    FloatLabelJSlider coneNUScaleZSlider = new FloatLabelJSlider("Z: ",
        0.1f, 0.0f, 3.0f, (float) coneNUScale.z);
    coneNUScaleZSlider.setMajorTickSpacing(1.0f);
    coneNUScaleZSlider.setPaintTicks(true);
    coneNUScaleZSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneNUScale.z = (double) e.getValue();
        useUniformScale = false;
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeNUScale();
        }
      }
    });
    panel.add(coneNUScaleZSlider);

    return panel;
  }

  JPanel rotationPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    panel.add(new LeftAlignComponent(new JLabel("Rotation Axis")));
    FloatLabelJSlider coneRotateAxisXSlider = new FloatLabelJSlider("X: ",
        0.01f, -1.0f, 1.0f, (float) coneRotateAxis.x);
    coneRotateAxisXSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRotateAxis.x = e.getValue();
        normalizeConeRotateAxis();
        updateConeAxisAngle();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeRotation();
        }
        rotAxis.setRotationAxis(coneRotateAxis);
        updateConeRotateNormalizedLabels();
      }
    });
    panel.add(coneRotateAxisXSlider);

    FloatLabelJSlider coneRotateAxisYSlider = new FloatLabelJSlider("Y: ",
        0.01f, -1.0f, 1.0f, (float) coneRotateAxis.y);
    coneRotateAxisYSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRotateAxis.y = e.getValue();
        normalizeConeRotateAxis();
        updateConeAxisAngle();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeRotation();
        }
        rotAxis.setRotationAxis(coneRotateAxis);
        updateConeRotateNormalizedLabels();
      }
    });
    panel.add(coneRotateAxisYSlider);

    FloatLabelJSlider coneRotateAxisZSlider = new FloatLabelJSlider("Z: ",
        0.01f, -1.0f, 1.0f, (float) coneRotateAxis.y);
    coneRotateAxisZSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRotateAxis.z = e.getValue();
        normalizeConeRotateAxis();
        updateConeAxisAngle();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeRotation();
        }
        rotAxis.setRotationAxis(coneRotateAxis);
        updateConeRotateNormalizedLabels();
      }
    });
    panel.add(coneRotateAxisZSlider);

    JLabel normalizedLabel = new JLabel("Normalized Rotation Axis");
    panel.add(new LeftAlignComponent(normalizedLabel));
    ;
    coneRotateNAxisXLabel = new JLabel("X: 1.000");
    panel.add(new LeftAlignComponent(coneRotateNAxisXLabel));
    coneRotateNAxisYLabel = new JLabel("Y: 0.000");
    panel.add(new LeftAlignComponent(coneRotateNAxisYLabel));
    coneRotateNAxisZLabel = new JLabel("Z: 0.000");
    panel.add(new LeftAlignComponent(coneRotateNAxisZLabel));
    normalizeConeRotateAxis();
    updateConeRotateNormalizedLabels();

    FloatLabelJSlider coneRotateAxisAngleSlider = new FloatLabelJSlider(
        "Angle: ", 1.0f, -180.0f, 180.0f, (float) coneRotateAngle);
    coneRotateAxisAngleSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRotateAngle = e.getValue();
        updateConeAxisAngle();
        if (useCompoundTransform) {
          updateUsingCompoundTransform();
        } else {
          setConeRotation();
        }
      }
    });
    panel.add(coneRotateAxisAngleSlider);

    return panel;
  }

  Box refPtPanel() {
    Box panel = new Box(BoxLayout.Y_AXIS);

    panel.add(new LeftAlignComponent(new JLabel(
        "Reference Point Coordinates")));

    // X Ref Pt
    FloatLabelJSlider coneRefPtXSlider = new FloatLabelJSlider("X", 0.1f,
        -2.0f, 2.0f, coneRefPt.x);
    coneRefPtXSlider.setMajorTickSpacing(1.0f);
    coneRefPtXSlider.setPaintTicks(true);
    coneRefPtXSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRefPt.x = e.getValue();
        useCompoundTransform = true;
        updateUsingCompoundTransform();
        rotAxis.setRefPt(coneRefPt);
      }
    });
    panel.add(coneRefPtXSlider);

    // Y Ref Pt
    FloatLabelJSlider coneRefPtYSlider = new FloatLabelJSlider("Y", 0.1f,
        -2.0f, 2.0f, coneRefPt.y);
    coneRefPtYSlider.setMajorTickSpacing(1.0f);
    coneRefPtYSlider.setPaintTicks(true);
    coneRefPtYSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRefPt.y = e.getValue();
        useCompoundTransform = true;
        updateUsingCompoundTransform();
        rotAxis.setRefPt(coneRefPt);
      }
    });
    panel.add(coneRefPtYSlider);

    // Z Ref Pt
    FloatLabelJSlider coneRefPtZSlider = new FloatLabelJSlider("Z", 0.1f,
        -2.0f, 2.0f, coneRefPt.z);
    coneRefPtZSlider.setMajorTickSpacing(1.0f);
    coneRefPtZSlider.setPaintTicks(true);
    coneRefPtZSlider.addFloatListener(new FloatListener() {
      public void floatChanged(FloatEvent e) {
        coneRefPt.z = e.getValue();
        useCompoundTransform = true;
        updateUsingCompoundTransform();
        rotAxis.setRefPt(coneRefPt);
      }
    });
    panel.add(coneRefPtZSlider);

    return panel;
  }

  JPanel configPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));
    panel.add(new JLabel("Display annotation:"));

    // create the check boxes
    rotAxisCheckBox = new JCheckBox(rotAxisString);
    rotAxisCheckBox.setSelected(showRotAxis);
    rotAxisCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        showRotAxis = ((JCheckBox) source).isSelected();
        if (showRotAxis) {
          rotAxis.setWhichChild(Switch.CHILD_ALL);
        } else {
          rotAxis.setWhichChild(Switch.CHILD_NONE);
        }
      }
    });
    panel.add(rotAxisCheckBox);

    coordSysCheckBox = new JCheckBox(coordSysString);
    coordSysCheckBox.setSelected(showCoordSys);
    coordSysCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        showCoordSys = ((JCheckBox) source).isSelected();
        if (showCoordSys) {
          coordSys.setWhichChild(Switch.CHILD_ALL);
        } else {
          coordSys.setWhichChild(Switch.CHILD_NONE);
        }
      }
    });
    panel.add(coordSysCheckBox);

    if (isApplication) {
      JButton snapButton = new JButton(snapImageString);
      snapButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Point loc = canvas.getLocationOnScreen();
          offScreenCanvas.setOffScreenLocation(loc);
          Dimension dim = canvas.getSize();
          dim.width *= offScreenScale;
          dim.height *= offScreenScale;
          nf.setMinimumIntegerDigits(3);
          nf.setMaximumFractionDigits(0);
          offScreenCanvas.snapImageFile(outFileBase
              + nf.format(outFileSeq++), dim.width, dim.height);
          nf.setMinimumIntegerDigits(0);
        }
      });
      panel.add(snapButton);
    }

    return panel;
  }

  public void destroy() {
    u.removeAllLocales();
  }

  // The following allows TransformExplorer to be run as an application
  // as well as an applet
  //
  public static void main(String[] args) {
    float initOffScreenScale = 2.5f;
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-s")) {
        if (args.length >= (i + 1)) {
          initOffScreenScale = Float.parseFloat(args[i + 1]);
          i++;
        }
      }
    }
    new MainFrame(new TransformExplorer(true, initOffScreenScale), 950, 600);
  }
}

interface Java3DExplorerConstants {

  // colors
  static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

  static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);

  static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

  static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);

  static Color3f skyBlue = new Color3f(0.6f, 0.7f, 0.9f);

  static Color3f cyan = new Color3f(0.0f, 1.0f, 1.0f);

  static Color3f magenta = new Color3f(1.0f, 0.0f, 1.0f);

  static Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);

  static Color3f brightWhite = new Color3f(1.0f, 1.5f, 1.5f);

  static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  static Color3f darkGrey = new Color3f(0.15f, 0.15f, 0.15f);

  static Color3f medGrey = new Color3f(0.3f, 0.3f, 0.3f);

  static Color3f grey = new Color3f(0.5f, 0.5f, 0.5f);

  static Color3f lightGrey = new Color3f(0.75f, 0.75f, 0.75f);

  // infinite bounding region, used to make env nodes active everywhere
  BoundingSphere infiniteBounds = new BoundingSphere(new Point3d(),
      Double.MAX_VALUE);

  // common values
  static final String nicestString = "NICEST";

  static final String fastestString = "FASTEST";

  static final String antiAliasString = "Anti-Aliasing";

  static final String noneString = "NONE";

  // light type constants
  static int LIGHT_AMBIENT = 1;

  static int LIGHT_DIRECTIONAL = 2;

  static int LIGHT_POSITIONAL = 3;

  static int LIGHT_SPOT = 4;

  // screen capture constants
  static final int USE_COLOR = 1;

  static final int USE_BLACK_AND_WHITE = 2;

  // number formatter
  NumberFormat nf = NumberFormat.getInstance();

}

class OffScreenCanvas3D extends Canvas3D {

  OffScreenCanvas3D(GraphicsConfiguration graphicsConfiguration,
      boolean offScreen) {

    super(graphicsConfiguration, offScreen);
  }

  private BufferedImage doRender(int width, int height) {

    BufferedImage bImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);

    ImageComponent2D buffer = new ImageComponent2D(
        ImageComponent.FORMAT_RGB, bImage);
    //buffer.setYUp(true);

    setOffScreenBuffer(buffer);
    renderOffScreenBuffer();
    waitForOffScreenRendering();
    bImage = getOffScreenBuffer().getImage();
    return bImage;
  }

  void snapImageFile(String filename, int width, int height) {
    BufferedImage bImage = doRender(width, height);
    File file = new File(filename + ".jpg");

    try {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No se encontró un escritor para JPEG.");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1.0f); // Calidad máxima
        }

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(bImage, null, null), param);
        }
        writer.dispose();
    } catch (IOException e) {
    }
}
  
}

class FloatLabelJSlider extends JPanel implements ChangeListener,
    Java3DExplorerConstants {

  JSlider slider;

  JLabel valueLabel;

  Vector listeners = new Vector();

  float min, max, resolution, current, scale;

  int minInt, maxInt, curInt;;

  int intDigits, fractDigits;

  float minResolution = 0.001f;

  // default slider with name, resolution = 0.1, min = 0.0, max = 1.0 inital
  // 0.5
  FloatLabelJSlider(String name) {
    this(name, 0.1f, 0.0f, 1.0f, 0.5f);
  }

  FloatLabelJSlider(String name, float resolution, float min, float max,
      float current) {

    this.resolution = resolution;
    this.min = min;
    this.max = max;
    this.current = current;

    if (resolution < minResolution) {
      resolution = minResolution;
    }

    // round scale to nearest integer fraction. i.e. 0.3 => 1/3 = 0.33
    scale = (float) Math.round(1.0f / resolution);
    resolution = 1.0f / scale;

    // get the integer versions of max, min, current
    minInt = Math.round(min * scale);
    maxInt = Math.round(max * scale);
    curInt = Math.round(current * scale);

    // sliders use integers, so scale our floating point value by "scale"
    // to make each slider "notch" be "resolution". We will scale the
    // value down by "scale" when we get the event.
    slider = new JSlider(JSlider.HORIZONTAL, minInt, maxInt, curInt);
    slider.addChangeListener(this);

    valueLabel = new JLabel(" ");

    // set the initial value label
    setLabelString();

    // add min and max labels to the slider
    Hashtable labelTable = new Hashtable();
    labelTable.put(new Integer(minInt), new JLabel(nf.format(min)));
    labelTable.put(new Integer(maxInt), new JLabel(nf.format(max)));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);

    /* layout to align left */
    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(new JLabel(name));
    box.add(slider);
    box.add(valueLabel);
  }

  public void setMinorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMinorTickSpacing(intSpacing);
  }

  public void setMajorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMajorTickSpacing(intSpacing);
  }

  public void setPaintTicks(boolean paint) {
    slider.setPaintTicks(paint);
  }

  public void addFloatListener(FloatListener listener) {
    listeners.add(listener);
  }

  public void removeFloatListener(FloatListener listener) {
    listeners.remove(listener);
  }

  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    // get the event type, set the corresponding value.
    // Sliders use integers, handle floating point values by scaling the
    // values by "scale" to allow settings at "resolution" intervals.
    // Divide by "scale" to get back to the real value.
    curInt = source.getValue();
    current = curInt / scale;

    valueChanged();
  }

  public void setValue(float newValue) {
    boolean changed = (newValue != current);
    current = newValue;
    if (changed) {
      valueChanged();
    }
  }

  private void valueChanged() {
    // update the label
    setLabelString();

    // notify the listeners
    FloatEvent event = new FloatEvent(this, current);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      FloatListener listener = (FloatListener) e.nextElement();
      listener.floatChanged(event);
    }
  }

  void setLabelString() {
    // Need to muck around to try to make sure that the width of the label
    // is wide enough for the largest value. Pad the string
    // be large enough to hold the largest value.
    int pad = 5; // fudge to make up for variable width fonts
    float maxVal = Math.max(Math.abs(min), Math.abs(max));
    intDigits = Math.round((float) (Math.log(maxVal) / Math.log(10))) + pad;
    if (min < 0) {
      intDigits++; // add one for the '-'
    }
    // fractDigits is num digits of resolution for fraction. Use base 10 log
    // of scale, rounded up, + 2.
    fractDigits = (int) Math.ceil((Math.log(scale) / Math.log(10)));
    nf.setMinimumFractionDigits(fractDigits);
    nf.setMaximumFractionDigits(fractDigits);
    String value = nf.format(current);
    while (value.length() < (intDigits + fractDigits)) {
      value = value + "  ";
    }
    valueLabel.setText(value);
  }

}

class FloatEvent extends EventObject {

  float value;

  FloatEvent(Object source, float newValue) {
    super(source);
    value = newValue;
  }

  float getValue() {
    return value;
  }
}

interface FloatListener extends EventListener {
  void floatChanged(FloatEvent e);
}

class LogFloatLabelJSlider extends JPanel implements ChangeListener,
    Java3DExplorerConstants {

  JSlider slider;

  JLabel valueLabel;

  Vector listeners = new Vector();

  float min, max, resolution, current, scale;

  double minLog, maxLog, curLog;

  int minInt, maxInt, curInt;;

  int intDigits, fractDigits;

  NumberFormat nf = NumberFormat.getInstance();

  float minResolution = 0.001f;

  double logBase = Math.log(10);

  // default slider with name, resolution = 0.1, min = 0.0, max = 1.0 inital
  // 0.5
  LogFloatLabelJSlider(String name) {
    this(name, 0.1f, 100.0f, 10.0f);
  }

  LogFloatLabelJSlider(String name, float min, float max, float current) {

    this.resolution = resolution;
    this.min = min;
    this.max = max;
    this.current = current;

    if (resolution < minResolution) {
      resolution = minResolution;
    }

    minLog = log10(min);
    maxLog = log10(max);
    curLog = log10(current);

    // resolution is 100 steps from min to max
    scale = 100.0f;
    resolution = 1.0f / scale;

    // get the integer versions of max, min, current
    minInt = (int) Math.round(minLog * scale);
    maxInt = (int) Math.round(maxLog * scale);
    curInt = (int) Math.round(curLog * scale);

    slider = new JSlider(JSlider.HORIZONTAL, minInt, maxInt, curInt);
    slider.addChangeListener(this);

    valueLabel = new JLabel(" ");

    // Need to muck around to make sure that the width of the label
    // is wide enough for the largest value. Pad the initial string
    // be large enough to hold the largest value.
    int pad = 5; // fudge to make up for variable width fonts
    intDigits = (int) Math.ceil(maxLog) + pad;
    if (min < 0) {
      intDigits++; // add one for the '-'
    }
    if (minLog < 0) {
      fractDigits = (int) Math.ceil(-minLog);
    } else {
      fractDigits = 0;
    }
    nf.setMinimumFractionDigits(fractDigits);
    nf.setMaximumFractionDigits(fractDigits);
    String value = nf.format(current);
    while (value.length() < (intDigits + fractDigits)) {
      value = value + " ";
    }
    valueLabel.setText(value);

    // add min and max labels to the slider
    Hashtable labelTable = new Hashtable();
    labelTable.put(new Integer(minInt), new JLabel(nf.format(min)));
    labelTable.put(new Integer(maxInt), new JLabel(nf.format(max)));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);

    // layout to align left
    setLayout(new BorderLayout());
    Box box = new Box(BoxLayout.X_AXIS);
    add(box, BorderLayout.WEST);

    box.add(new JLabel(name));
    box.add(slider);
    box.add(valueLabel);
  }

  public void setMinorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMinorTickSpacing(intSpacing);
  }

  public void setMajorTickSpacing(float spacing) {
    int intSpacing = Math.round(spacing * scale);
    slider.setMajorTickSpacing(intSpacing);
  }

  public void setPaintTicks(boolean paint) {
    slider.setPaintTicks(paint);
  }

  public void addFloatListener(FloatListener listener) {
    listeners.add(listener);
  }

  public void removeFloatListener(FloatListener listener) {
    listeners.remove(listener);
  }

  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    curInt = source.getValue();
    curLog = curInt / scale;
    current = (float) exp10(curLog);

    valueChanged();
  }

  public void setValue(float newValue) {
    boolean changed = (newValue != current);
    current = newValue;
    if (changed) {
      valueChanged();
    }
  }

  private void valueChanged() {
    String value = nf.format(current);
    valueLabel.setText(value);

    // notify the listeners
    FloatEvent event = new FloatEvent(this, current);
    for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
      FloatListener listener = (FloatListener) e.nextElement();
      listener.floatChanged(event);
    }
  }

  double log10(double value) {
    return Math.log(value) / logBase;
  }

  double exp10(double value) {
    return Math.exp(value * logBase);
  }

}

class CoordSys extends Switch {

  // Temporaries that are reused
  Transform3D tmpTrans = new Transform3D();

  Vector3f tmpVector = new Vector3f();

  AxisAngle4f tmpAxisAngle = new AxisAngle4f();

  // colors for use in the shapes
  Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

  Color3f grey = new Color3f(0.3f, 0.3f, 0.3f);

  Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

  // geometric constants
  Point3f origin = new Point3f();

  Vector3f yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

  CoordSys(float axisLength) {
    super(Switch.CHILD_ALL);

    float coordSysLength = axisLength;
    float labelOffset = axisLength / 20.0f;
    float axisRadius = axisLength / 500.0f;
    float arrowRadius = axisLength / 125.0f;
    float arrowHeight = axisLength / 50.0f;
    float tickRadius = axisLength / 125.0f;
    float tickHeight = axisLength / 250.0f;

    // Set the Switch to allow changes
    setCapability(Switch.ALLOW_SWITCH_READ);
    setCapability(Switch.ALLOW_SWITCH_WRITE);

    // Set up an appearance to make the Axis have
    // grey ambient, black emmissive, grey diffuse and grey specular
    // coloring.
    //Material material = new Material(grey, black, grey, white, 64);
    Material material = new Material(white, black, white, white, 64);
    Appearance appearance = new Appearance();
    appearance.setMaterial(material);

    // Create a shared group to hold one axis of the coord sys
    SharedGroup coordAxisSG = new SharedGroup();

    // create a cylinder for the central line of the axis
    Cylinder cylinder = new Cylinder(axisRadius, coordSysLength, appearance);
    // cylinder goes from -coordSysLength/2 to coordSysLength in y
    coordAxisSG.addChild(cylinder);

    // create the shared arrowhead
    Cone arrowHead = new Cone(arrowRadius, arrowHeight, appearance);
    SharedGroup arrowHeadSG = new SharedGroup();
    arrowHeadSG.addChild(arrowHead);

    // Create a TransformGroup to move the arrowhead to the top of the
    // axis
    // The arrowhead goes from -arrowHeight/2 to arrowHeight/2 in y.
    // Put it at the top of the axis, coordSysLength / 2
    tmpVector.set(0.0f, coordSysLength / 2 + arrowHeight / 2, 0.0f);
    tmpTrans.set(tmpVector);
    TransformGroup topTG = new TransformGroup();
    topTG.setTransform(tmpTrans);
    topTG.addChild(new Link(arrowHeadSG));
    coordAxisSG.addChild(topTG);

    // create the minus arrowhead
    // Create a TransformGroup to turn the cone upside down:
    // Rotate 180 degrees around Z axis
    tmpAxisAngle.set(0.0f, 0.0f, 1.0f, (float) Math.toRadians(180));
    tmpTrans.set(tmpAxisAngle);

    // Put the arrowhead at the bottom of the axis
    tmpVector.set(0.0f, -coordSysLength / 2 - arrowHeight / 2, 0.0f);
    tmpTrans.setTranslation(tmpVector);
    TransformGroup bottomTG = new TransformGroup();
    bottomTG.setTransform(tmpTrans);
    bottomTG.addChild(new Link(arrowHeadSG));
    coordAxisSG.addChild(bottomTG);

    // Now add "ticks" at 1, 2, 3, etc.

    // create a shared group for the tick
    Cylinder tick = new Cylinder(tickRadius, tickHeight, appearance);
    SharedGroup tickSG = new SharedGroup();
    tickSG.addChild(tick);

    // transform each instance and add it to the coord axis group
    int maxTick = (int) (coordSysLength / 2);
    int minTick = -maxTick;
    for (int i = minTick; i <= maxTick; i++) {
      if (i == 0)
        continue; // no tick at 0

      // use a TransformGroup to offset to the tick location
      TransformGroup tickTG = new TransformGroup();
      tmpVector.set(0.0f, (float) i, 0.0f);
      tmpTrans.set(tmpVector);
      tickTG.setTransform(tmpTrans);
      // then link to an instance of the Tick shared group
      tickTG.addChild(new Link(tickSG));
      // add the TransformGroup to the coord axis
      coordAxisSG.addChild(tickTG);
    }

    // add a Link to the axis SharedGroup to the coordSys
    addChild(new Link(coordAxisSG)); // Y axis

    // Create TransformGroups for the X and Z axes
    TransformGroup xAxisTG = new TransformGroup();
    // rotate 90 degrees around Z axis
    tmpAxisAngle.set(0.0f, 0.0f, 1.0f, (float) Math.toRadians(90));
    tmpTrans.set(tmpAxisAngle);
    xAxisTG.setTransform(tmpTrans);
    xAxisTG.addChild(new Link(coordAxisSG));
    addChild(xAxisTG); // X axis

    TransformGroup zAxisTG = new TransformGroup();
    // rotate 90 degrees around X axis
    tmpAxisAngle.set(1.0f, 0.0f, 0.0f, (float) Math.toRadians(90));
    tmpTrans.set(tmpAxisAngle);
    zAxisTG.setTransform(tmpTrans);
    zAxisTG.addChild(new Link(coordAxisSG));
    addChild(zAxisTG); // Z axis

    // Add the labels. First we need a Font3D for the Text3Ds
    // select the default font, plain style, 0.5 tall. Use null for
    // the extrusion so we get "flat" text since we will be putting it
    // into an oriented Shape3D
    Font3D f3d = new Font3D(new Font("Default", Font.PLAIN, 1), null);

    // set up the +X label
    Text3D plusXText = new Text3D(f3d, "+X", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D plusXTextShape = new OrientedShape3D(plusXText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup plusXTG = new TransformGroup();
    tmpVector.set(coordSysLength / 2 + labelOffset, 0.0f, 0.0f);
    tmpTrans.set(0.15f, tmpVector);
    plusXTG.setTransform(tmpTrans);
    plusXTG.addChild(plusXTextShape);
    addChild(plusXTG);

    // set up the -X label
    Text3D minusXText = new Text3D(f3d, "-X", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D minusXTextShape = new OrientedShape3D(minusXText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup minusXTG = new TransformGroup();
    tmpVector.set(-coordSysLength / 2 - labelOffset, 0.0f, 0.0f);
    tmpTrans.set(0.15f, tmpVector);
    minusXTG.setTransform(tmpTrans);
    minusXTG.addChild(minusXTextShape);
    addChild(minusXTG);

    // set up the +Y label
    Text3D plusYText = new Text3D(f3d, "+Y", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D plusYTextShape = new OrientedShape3D(plusYText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup plusYTG = new TransformGroup();
    tmpVector.set(0.0f, coordSysLength / 2 + labelOffset, 0.0f);
    tmpTrans.set(0.15f, tmpVector);
    plusYTG.setTransform(tmpTrans);
    plusYTG.addChild(plusYTextShape);
    addChild(plusYTG);

    // set up the -Y label
    Text3D minusYText = new Text3D(f3d, "-Y", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D minusYTextShape = new OrientedShape3D(minusYText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup minusYTG = new TransformGroup();
    tmpVector.set(0.0f, -coordSysLength / 2 - labelOffset, 0.0f);
    tmpTrans.set(0.15f, tmpVector);
    minusYTG.setTransform(tmpTrans);
    minusYTG.addChild(minusYTextShape);
    addChild(minusYTG);

    // set up the +Z label
    Text3D plusZText = new Text3D(f3d, "+Z", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D plusZTextShape = new OrientedShape3D(plusZText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup plusZTG = new TransformGroup();
    tmpVector.set(0.0f, 0.0f, coordSysLength / 2 + labelOffset);
    tmpTrans.set(0.15f, tmpVector);
    plusZTG.setTransform(tmpTrans);
    plusZTG.addChild(plusZTextShape);
    addChild(plusZTG);

    // set up the -Z label
    Text3D minusZText = new Text3D(f3d, "-Z", origin, Text3D.ALIGN_CENTER,
        Text3D.PATH_RIGHT);
    // orient around the local origin
    OrientedShape3D minusZTextShape = new OrientedShape3D(minusZText,
        appearance, OrientedShape3D.ROTATE_ABOUT_POINT, origin);
    // transform to scale down to 0.15 in height, locate at end of axis
    TransformGroup minusZTG = new TransformGroup();
    tmpVector.set(0.0f, 0.0f, -coordSysLength / 2 - labelOffset);
    tmpTrans.set(0.15f, tmpVector);
    minusZTG.setTransform(tmpTrans);
    minusZTG.addChild(minusZTextShape);
    addChild(minusZTG);
  }
}

class LeftAlignComponent extends JPanel {
  LeftAlignComponent(Component c) {
    setLayout(new BorderLayout());
    add(c, BorderLayout.WEST);
  }
}

class RotAxis extends Switch implements Java3DExplorerConstants {


    // axis to align with 
    Vector3f    rotAxis = new Vector3f(1.0f, 0.0f, 0.0f); 
    // offset to ref point 
    Vector3f    refPt = new Vector3f(0.0f, 0.0f, 0.0f); 

    TransformGroup  axisTG; // the transform group used to align the axis

    // Temporaries that are reused
    Transform3D    tmpTrans = new Transform3D();
    Vector3f    tmpVector = new Vector3f();
    AxisAngle4f    tmpAxisAngle = new AxisAngle4f();

    // geometric constants
    Point3f    origin = new Point3f();
    Vector3f    yAxis = new Vector3f(0.0f, 1.0f, 0.0f);

    RotAxis(float axisLength) {
  super(Switch.CHILD_NONE);
  setCapability(Switch.ALLOW_SWITCH_READ);
  setCapability(Switch.ALLOW_SWITCH_WRITE);

  // set up the proportions for the arrow
  float axisRadius = axisLength / 120.0f;
  float arrowRadius = axisLength / 50.0f;
  float arrowHeight = axisLength / 30.0f;


  // create the TransformGroup which will be used to orient the axis
  axisTG = new TransformGroup();
  axisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  axisTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  addChild(axisTG);

  // Set up an appearance to make the Axis have 
  // blue ambient, black emmissive, blue diffuse and white specular 
  // coloring.  
  Material material = new Material(blue, black, blue, white, 64);
  Appearance appearance = new Appearance();
  appearance.setMaterial(material);

  // create a cylinder for the central line of the axis
  Cylinder cylinder = new Cylinder(axisRadius, axisLength, appearance); 
  // cylinder goes from -length/2 to length/2 in y
  axisTG.addChild(cylinder);

  // create a SharedGroup for the arrowHead
  Cone arrowHead = new Cone(arrowRadius, arrowHeight, appearance); 
  SharedGroup arrowHeadSG = new SharedGroup();
  arrowHeadSG.addChild(arrowHead);

  // Create a TransformGroup to move the cone to the top of the 
  // cylinder
  tmpVector.set(0.0f, axisLength/2 + arrowHeight / 2, 0.0f);
  tmpTrans.set(tmpVector);
  TransformGroup topTG = new TransformGroup();  
  topTG.setTransform(tmpTrans);
  topTG.addChild(new Link(arrowHeadSG));
  axisTG.addChild(topTG);

  // create the bottom of the arrow
  // Create a TransformGroup to move the cone to the bottom of the 
  // axis so that its pushes into the bottom of the cylinder
  tmpVector.set(0.0f, -(axisLength / 2), 0.0f);
  tmpTrans.set(tmpVector);
  TransformGroup bottomTG = new TransformGroup();  
  bottomTG.setTransform(tmpTrans);
  bottomTG.addChild(new Link(arrowHeadSG));
  axisTG.addChild(bottomTG);

  updateAxisTransform();
    }

    public void setRotationAxis(Vector3f setRotAxis) {
  rotAxis.set(setRotAxis);
  float magSquared = rotAxis.lengthSquared();
  if (magSquared > 0.0001) {
      rotAxis.scale((float)(1.0 / Math.sqrt(magSquared)));
  } else {
      rotAxis.set(1.0f, 0.0f, 0.0f);
  }
  updateAxisTransform();
    }

    public void setRefPt(Vector3f setRefPt) {
  refPt.set(setRefPt);
  updateAxisTransform();
    }

    // set the transform on the axis so that it aligns with the rotation
    // axis and goes through the reference point
    private void updateAxisTransform() {
  // We need to rotate the axis, which is defined along the y-axis,
  // to the direction indicated by the rotAxis.
  // We can do this using a neat trick.  To transform a vector to align
  // with another vector (assuming both vectors have unit length), take 
  // the cross product the the vectors.  The direction of the cross
  // product is the axis, and the length of the cross product is the
  // the sine of the angle, so the inverse sine of the length gives 
  // us the angle
  tmpVector.cross(yAxis, rotAxis);
  float angle = (float)Math.asin(tmpVector.length());

  tmpAxisAngle.set(tmpVector, angle);
  tmpTrans.set(tmpAxisAngle);
  tmpTrans.setTranslation(refPt);
  axisTG.setTransform(tmpTrans);
    }
}