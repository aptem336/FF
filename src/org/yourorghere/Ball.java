package org.yourorghere;

public class Ball extends Moveable {

    //��������� ���������� �������
    private static final double G = 9.8d;
    //������
    public double R;

    public Ball(double R, double density) {
        super();
        //�������� ������
        this.R = R;
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
