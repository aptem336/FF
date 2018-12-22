package org.yourorghere;

public class Ball {

    //��������� ���������� �������
    private static final double G = 9.8d;
    //������
    public double R;
    //�������:
    //���������
    public final Vector location;
    //��������
    public final Vector velocity;
    //���������������
    public final Vector pvelocity;
    //���������������� �����, ��� ���������� ������� ������� � ������ � ����� ����
    public final double iMass;

    public Ball(double R, double density) {
        //�������� ������
        this.R = R;
        //�������� ������ ��� �������
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
        //����� ����� ������ ����
        iMass = 1.0d / (4.0d / 3.0d * Math.PI * Math.pow(R, 3) * density);
    }

    //���������� ������
    public void integrate() {
        //�������� �� ��� Y ����������� �� G (���������� ��������� ����)
        velocity.y -= G;
        //��������� � ��������� ��������������
        location.add(pvelocity);
        //�������� ��������������
        pvelocity.set(Vector.NULL);
        //��������� �������� * ����� �������
        location.add(velocity, 1.0d / 100.0d);
    }
}
