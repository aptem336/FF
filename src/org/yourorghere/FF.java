package org.yourorghere;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class FF implements GLEventListener {

    public static void main(String[] args) {
        //������ ����
        Frame frame = new Frame("Body collision simulation");
        //�������� ������� ������
        int size = Toolkit.getDefaultToolkit().getScreenSize().height - 30;
        frame.setSize(size, size);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //������ ����� - ����� ���������
        GLCanvas canvas = new GLCanvas();
        //������ � ��������� ����������
        Listener listener = new Listener();
        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        canvas.addMouseWheelListener(listener);
        canvas.addGLEventListener(new FF());
        canvas.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 30);
        //������ ����������� �������� ��������� ��������
        Animator animator = new Animator(canvas);

        frame.add(canvas);
        //��������� ��������� ����
        frame.addWindowListener(new WindowAdapter() {
            @Override
            //�������������� �������� �������� ����
            public void windowClosing(WindowEvent e) {
                new Thread(() -> {
                    //������������� ��������
                    animator.stop();
                    //������� �� �������� 0 (��� ������)
                    System.exit(0);
                }).start();
            }
        });
        //��������� ��������
        animator.start();
        //��������� ���������
        Terrain.load("hm.raw", 2048, 64);
    }

    public static GL gl;
    private static GLU glu;
    public static GLUT glut;

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        //�������� ����
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        //����������� ��������
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glEnable(GL.GL_NORMALIZE);
        //������������ ����
        gl.glAlphaFunc(GL.GL_GREATER, 0);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_ALPHA_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        //�������� ���� �������
        gl.glEnable(GL.GL_DEPTH_TEST);
        //������� ����� ���� ����� ������
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 100000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    //���������� �� ������ �� �������
    public static double len = 2500d;
    //������������ � �������������� ���� � ����� �� ������� ������������ ������
    public static double angleV = 45d, angleH = 270d;
    //��������� ������ � ���� �����
    private static double xView, zView, yView;

    private static void calcCam() {
        //��������� ���������� ����� ������������ � �������������� ����
        xView = len * Math.sin(Math.toRadians(angleV)) * Math.cos(Math.toRadians(angleH));
        zView = 1024d + len * Math.sin(Math.toRadians(angleV)) * Math.sin(Math.toRadians(angleH));
        yView = -512d + len * Math.cos(Math.toRadians(angleV));
    }

    //���������� ��� �������� ���
    private static long time;
    private static long lc_time;
    private static int frames = 60;
    public static int fps;

    @Override
    public void display(GLAutoDrawable drawable) {
        //������� ������ ����� � �������
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //������������� ��������� ��������� ���������
        gl.glLightiv(GL.GL_LIGHT0, GL.GL_POSITION, new int[]{1, 1, 1, 0}, 0);
        gl.glLoadIdentity();
        gl.glTranslated(0d, 0d, 0d);

        //��������� ��������� ������
        calcCam();
        glu.gluLookAt(xView, yView, zView, 0d, -512d, 1024d, 0d, 0.5d, 0d);
        //��������� ����������� ����������� �������� �����
        Solver.step();
        //������������ ��� ������������
        for (Triangle triangle : Terrain.triangles) {
            Build.buildTriangle(triangle.vertex, triangle.planeNormal, Triangle.COLOR);
        }
        //���� ��� �������� fps
        //������� �����
        time = System.currentTimeMillis();
        //���� ������ ������ �������
        if (time - lc_time >= 1000) {
            //�������� fps ����� ����������� ������
            fps = frames;
            //��������� ����� ��� ���������� �������
            lc_time = time;
            //�������� ���������� �������
            frames = 0;
        }
        //����������� ���������� ������
        frames++;
        //�������� ����� ������ ��������
        drawText(drawable);
        gl.glFlush();
    }

    private static void drawText(GLAutoDrawable drawable) {
        gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glWindowPos2i(10, drawable.getHeight() - 45);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, fps + "   fps");

        gl.glWindowPos2i(10, drawable.getHeight() - 65);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.E + "   elasticity");

        gl.glWindowPos2i(10, drawable.getHeight() - 85);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, Solver.F + "   friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 45);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Esc - reset");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 65);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "W, S, A, D - rotate camera");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 85);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "R/T - +/- elasticity");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 105);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "F/G - +/- friction");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 125);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Space - pause");

        gl.glWindowPos2i(drawable.getWidth() - 180, drawable.getHeight() - 145);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Mouse wheel - add ball");
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
