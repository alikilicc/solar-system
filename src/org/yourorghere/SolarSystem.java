package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.Color;
import java.applet.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_MODELVIEW;
import static javax.media.opengl.GL.GL_PROJECTION;
import static javax.media.opengl.GL.GL_QUADS;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SolarSystem extends GLCanvas implements  GLEventListener {

    private Texture dunyadokusu, gunesdokusu, aydokusu;

    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static String BASLIK = "AY, DUNYA VE GUNES SISTEMI";
    private static  int CIZIMALANI_GENISLIK =(int) tk.getScreenSize().getWidth();
    private static int CIZIMALANI_YUKSEKLIK = (int) tk.getScreenSize().getHeight();
    private static int FPS = 60;

    private GLU glu;

    public SolarSystem() { //yap?land?r?c?
        this.addGLEventListener(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GLCanvas canvas = new SolarSystem(); // upcasting, s?n?ftan nesne
                canvas.setPreferredSize(new Dimension(CIZIMALANI_GENISLIK, CIZIMALANI_YUKSEKLIK)); // cizim alani boyut tanimlamasi
                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true); // cizim alani 
                JFrame frame = new JFrame(BASLIK); // pencere nesnesi
                frame.getContentPane().add(canvas); // cizim alaninin pencereye eklenmesi
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) { // program sonlandildiginda
                        new Thread() {
                            public void run() {
                                // çalisiyorsa durdur
                                System.exit(0);
                            }
                        }.start();
                    }
                });
                frame.pack(); // pencere boyutunu içeri?e gore ayarla
                frame.setVisible(true); // pencereyi görünür yap
                animator.start(); // çizimi baslat
                frame.setResizable(false);
            }
        });
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    // baslangic durumlari	
    public void init(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();
        glu = new GLU();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // arka plan rengi
        gl.glEnable(GL.GL_DEPTH_TEST);
        try {
            //InputStream stream = getClass().getResourceAsStream("C:\\Users\\Ali\\Documents\\NetBeansProjects\\Bilgisayarodev\\earth.png");
            File dos = new File("C:\\Ali\\NetBeansProjects\\Bilgisayarodev\\img\\earth.png");
            TextureData dosya2 = TextureIO.newTextureData(dos, false, "png");
            dunyadokusu = TextureIO.newTexture(dosya2);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        try {
            //InputStream stream = getClass().getResourceAsStream("C:\\Users\\Ali\\Documents\\NetBeansProjects\\Bilgisayarodev\\earth.png");
            File dos = new File("C:\\Ali\\NetBeansProjects\\Bilgisayarodev\\img\\indir.jpg");
            TextureData veri = TextureIO.newTextureData(dos, false, "jpg");
            gunesdokusu = TextureIO.newTexture(veri);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        try {
            //InputStream stream = getClass().getResourceAsStream("C:\\Users\\Ali\\Documents\\NetBeansProjects\\Bilgisayarodev\\earth.png");
            File dosya = new File("C:\\Ali\\NetBeansProjects\\Bilgisayarodev\\img\\moonmap.jpg");
            TextureData data = TextureIO.newTextureData(dosya, false, "jpg");
            aydokusu = TextureIO.newTexture(data);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

       
    }

    private void setCamera(GL gl, GLU glu, float uzaklik) {
        // Change to projection matrix.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float enboyorani = (float) getWidth() / (float) getHeight();
        glu.gluPerspective(45, enboyorani, 1, 1000);
        glu.gluLookAt(0, 0, uzaklik, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    // cizim alani degisince cizimi ölçekle

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    float donme = 0f, kendietrafindadonme = 0f;
    // Çizim	buradan yap?l?yor..................................................................................................

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //yeniden cizim icin tampon bellek temizligi
        gl.glLoadIdentity(); // matrisleri sifirla
        if ((drawable instanceof GLJPanel)
                && !((GLJPanel) drawable).isOpaque()
                && ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        } else {
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        }

        //güne? dünya ay kodlar?n ba?lan??c?
        //  //sahne
        // Set camera.
        setCamera(gl, glu, 80);
        gl.glTranslatef(0f, 0f, -170f);
        gl.glRotatef(donme, 0f, 1f, 0f); //güne?in Y ekseninde dönü?ü.
        gl.glColor3f(1f, 3.0f, 0.0f); //Güne?in rengi

        // Doku uygula.   
        gunesdokusu.enable();
        gunesdokusu.bind();
        GLUquadric gunes = glu.gluNewQuadric();
        glu.gluQuadricTexture(gunes, true);
        glu.gluSphere(gunes, 30f, 15, 15);
        glu.gluDeleteQuadric(gunes);

        gl.glRotatef(donme, 0f, 1f, 0f); //Y ekseninde döndürme kodunu yazd?k.
        gl.glTranslatef(20f, 0f, -80f); //Dünya'n?n sahnesini olu?turduk.
        gl.glRotatef(kendietrafindadonme, 0f, 1f, 0f); //Dünya'n?n kendi etraf?nda dönü?ü.

        // Doku uygula. 
        dunyadokusu.enable();
        dunyadokusu.bind();

        gl.glColor3f(0.3f, 0.5f, 1f);//Dünya'n?n rengi
        //Dünya'n?n çizimi
        GLUquadric dunya = glu.gluNewQuadric();
        glu.gluQuadricTexture(dunya, true);
        glu.gluSphere(dunya, 12f, 15, 15);
        glu.gluDeleteQuadric(dunya);

//Ay'a ait kodlar :
        gl.glRotatef(donme, 0f, 1f, 0f); //Ay'?n sahnesini Dünya ekseni etraf?nda döndürmek için.
        gl.glTranslatef(10f, 0f, -20f); //Ay'?n sahnesi
        gl.glColor3f(1f, 1f, 1f); //Ay'?n rengi
        gl.glRotatef(kendietrafindadonme, 0f, 1f, 0f); //Ay'?n kendi ekseni (Y ekseni) etraf?nda dönmesi için
// Doku uygula.   
        aydokusu.enable();
        aydokusu.bind();
//Ay'? olu?turan kodlar :
        GLUquadric ay = glu.gluNewQuadric();
        glu.gluQuadricTexture(ay, true);
        glu.gluSphere(ay, 3f, 15, 15);
        glu.gluDeleteQuadric(ay);

        //Güne? - Dünya - Ay Sistemine Ait Kodlar Biti?i
        donme += 0.1f; //dünyan?n güne? etraf?ndaki dönü?ü ve ay?n dünya etraf?ndaki dönü?ü
        kendietrafindadonme += 4f; //ay?n, dünyan?n ve güne?in kendi etraflar?ndaki dönü?leri.
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
