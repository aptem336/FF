package org.yourorghere;

import java.awt.Color;
import java.util.ArrayList;
import static org.yourorghere.FF.gl;
import static org.yourorghere.FF.glut;

public class Solver {

    //����������� ���������
    public static double E = 0.125d;
    //����������� ������
    public static double F = 0.125d;
    //����������� ������
    public static final Ball BALL = new Ball(25d);
    //������� ��������
    public static boolean move = false;
    //������ ��������� � �������������
    private static final ArrayList<ContactJoint> JOINTS = new ArrayList<>();
    //���������� �������� �� ���������� ���������, �������������
    private static final int IIC = 20, PIC = 20;

    public static void step() {
        JOINTS.clear();
        //��������� ��������� ���������
        Collision.generateJoints(BALL, JOINTS);
        //IIC ��� 
        for (int i = 0; i < IIC; i++) {
            //������ ��������
            JOINTS.forEach((joint) -> {
                joint.solveImpulse();
            });
        }
        //PPC ���
        for (int i = 0; i < PIC; i++) {
            //������ �������������
            JOINTS.forEach((joint) -> {
                joint.solvePenetration();
            });
        }
        //������������ �����
        buildSphere(BALL.location.x, BALL.location.y, BALL.location.z, BALL.R, Color.red);
        //���� �������� ���������
        if (move) {
            //����������� �������
            BALL.integrateLocation();
            //�����    
        } else {
            //������������ ����� ������
            Build.buildLine(new Vector[]{Vector.NULL, Vector.getProduct(Listener.diff, 0.25d)}, Color.red);
        }
    }

    private static final int SLICES = 20, STACKS = 20;

    private static void buildSphere(double x, double y, double z, double size, Color color) {
        gl.glPushMatrix();
        //��������� ����� � ����� (x, y, z)
        gl.glTranslated(x, y, z);
        //������������� ���� � float
        gl.glColor4d(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, color.getAlpha() / 255d);
        glut.glutSolidSphere(size, SLICES, STACKS);
        gl.glPopMatrix();
    }
}
