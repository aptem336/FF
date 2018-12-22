package org.yourorghere;

import java.util.ArrayList;

public class Collision {

    //��������� ��������� ���������
    public static void generateJoints(ArrayList<ContactJoint> JOINTS) {
        Solver.BALLS.forEach((ball) -> {
            //��������� �������� ��� ������� ������������
            for (Triangle triangle : Terrain.triangles) {
                //��������� ����� �� ����� ������������
                if (checkEdges(ball, triangle)) {
                    //��������� ����������� ��������� �����������
                    double deep = checkPlane(ball, triangle);
                    //���� ����� �� ��������� (������� > 0) �� �� ������ �������� �� �������
                    if (deep > 0 && deep < ball.R) {
                        //������ ����� �������
                        JOINTS.add(new ContactJoint(ball, triangle.planeNormal, deep));
                    }
                }
            }
        });
        //�������� �� ������� �����, ������ � ������
        for (int i = 0; i < Solver.BALLS.size() - 1; i++) {
            for (int j = i + 1; j < Solver.BALLS.size(); j++) {
                //�������� ��������� - �� �� ����� �������� ���������� ����� ����, � ����� ��� ����� ������������ � �������� ������� �������
                Vector diff = Vector.getDiff(Solver.BALLS.get(i).location, Solver.BALLS.get(j).location);
                //����� ��������
                double R = Solver.BALLS.get(i).R + Solver.BALLS.get(j).R;
                //���� ����� ������ ����� �������� - ���� ������������
                //����������, ���� ���������� ����� ������ �������� ����� �������� - ���� ������������
                if (diff.squreLen() <= R * R) {
                    //��������� �������, ������������� �������� ��������� - ������� ������, ������� ����� ������ �������� � � ������ - ������� ������������� � ���� �����
                    JOINTS.add(new ContactJoint(Solver.BALLS.get(i), Solver.BALLS.get(j), diff.getNormalized(), R - diff.len()));
                }
            }
        }
    }

    private static double checkPlane(Ball ball, Triangle triangle) {
        //���������� ������� �������������
        return triangle.planeDot - Vector.getDP(ball.location, triangle.planeNormal) + ball.R;
    }

    private static boolean checkEdges(Ball ball, Triangle triangle) {
        for (int i = 0; i < triangle.edgesDots.length; i++) {
            //���� �������� �� �����-���� ������� �� �������������
            if (triangle.edgesDots[i] - Vector.getDP(ball.location, triangle.edgesNormal[i]) + ball.R < 0) {
                //��������� false
                return false;
            }
        }
        return true;
    }
}
