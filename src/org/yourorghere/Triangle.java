package org.yourorghere;

import java.awt.Color;

public class Triangle {

    public static final Color COLOR = new Color(0xFFFFFF);
    //�������
    public final Vector[] vertex;
    //������� ���������
    public final Vector planeNormal;
    //��������� ������������ ������ ���� �� ��� �������
    public final double planeDot;
    //������� ����
    public final Vector[] edgesNormal;
    //��������� ������������ ���� �� ����������� �������
    public final double[] edgesDots;

    public Triangle(Vector[] vertex) {
        this.vertex = vertex;
        edgesNormal = new Vector[3];
        edgesDots = new double[3];
        //������� ������� ��������� ����� ��� �����
        planeNormal = Vector.getPlaneNormal(vertex[0], vertex[1], vertex[2]);
        //��������� (����� = 1)
        planeNormal.normalize();
        //�������� ��������� ������������
        planeDot = Vector.getDP(vertex[0], planeNormal);
        for (int i = 0; i < 3; i++) {
            //������� ����� ��������������� ������� ��������� � ������� ����� 
            edgesNormal[i] = Vector.getCrossProduct(Vector.getDiff(vertex[(i + 1) % 3], vertex[i]), planeNormal);
            //��������� 
            edgesNormal[i].normalize();
            //�������� ��������� ������������
            edgesDots[i] = Vector.getDP(vertex[i], edgesNormal[i]);
        }
    }
}
