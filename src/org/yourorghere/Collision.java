package org.yourorghere;

import java.util.ArrayList;

public class Collision {

    //��������� ��������� ���������
    public static void generateJoints(Ball ball, ArrayList<ContactJoint> JOINTS) {
        //��������� �������� ��� ������� ������������
        for (Triangle triangle : Terrain.triangles) {
            //��������� ����� �� ����� ������������
            if (checkEdges(ball, triangle)) {
                //��������� ����������� ��������� �����������
                double deep = checkPlane(ball, triangle);
                //���� ����� �� ��������� (������� > 0) �� �� ������ �������� �� �������
                if (deep > 0 && deep < Solver.BALL.R) {
                    //������ ����� �������
                    JOINTS.add(new ContactJoint(triangle.planeNormal, deep));
                }
            }
        }
    }

    private static double checkPlane(Ball ball, Triangle quad) {
        //���������� ������� �������������
        return quad.planeDot - Vector.getDP(ball.location, quad.planeNormal) + ball.R;
    }

    private static boolean checkEdges(Ball ball, Triangle quad) {
        for (int i = 0; i < 3; i++) {
            //���� �������� �� �����-���� ������� �� �������������
            if (quad.edgesDots[i] - Vector.getDP(ball.location, quad.edgesNormal[i]) + ball.R < 0) {
                //��������� false
                return false;
            }
        }
        return true;
    }
}
