package org.yourorghere;

import java.awt.Color;
import java.util.ArrayList;
import static org.yourorghere.BC.gl;
import static org.yourorghere.BC.glut;

public class Solver {

    //����������� ���������
    public static double E = 0.125d;
    //����������� ������
    public static double F = 0.125d;
    //������ �����
    public static final ArrayList<Ball> BALLS = new ArrayList<>();
    //������� ��������
    public static boolean move = true;
    //������ ��������� � �������������
    private static final ArrayList<ContactJoint> JOINTS = new ArrayList<>();
    //���������� �������� �� ���������� ���������, �������������
    private static final int IIC = 20, PIC = 20;
    //������������� ������
    public static boolean reset = true;

    public static void step() {
        //���� ������������� ������ �������
        if (reset) {
            //������ ������ ����
            BALLS.clear();
            //�����������
            reset = false;
        }
        JOINTS.clear();
        //��������� ��������� ���������
        Collision.generateJoints(JOINTS);
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
        //������������ ������ �����
        BALLS.forEach((ball) -> {
            buildSphere(ball.location.x, ball.location.y, ball.location.z, ball.R, Color.gray);
        });
        //���� �������� ���������
        if (move) {
            //����������� �������
            BALLS.forEach((ball) -> {
                ball.integrate();
            });
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
