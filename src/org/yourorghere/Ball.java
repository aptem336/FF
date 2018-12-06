package org.yourorghere;

public class Ball {

    //��������� ���������� �������
    private static final double G = 9.8d;
    //����� �������
    private static final double DT = 1d / 300d;
    //������
    public double R;
    //�������
    //���������
    public final Vector location;
    //��������
    public final Vector velocity;
    //���������������
    public final Vector pvelocity;

    public Ball(double R) {
        //�������� ������
        this.R = R;
        //�������� ������ ��� ������
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
    }
    
    //���������� ������
    public void integrateLocation() {
        //�������� �� ��� Y ����������� �� G (���������� ��������� ����)
        velocity.y -= G;
        //��������� � ��������� ��������������
        location.add(pvelocity);
        //�������� ��������������
        pvelocity.set(Vector.NULL);
        //��������� �������� * ����� �������
        location.add(velocity, DT);
    }

    //������
    public void toss(Vector toss) {
        //��������� � �������� ������� ������
        this.velocity.add(toss);
    }
}
