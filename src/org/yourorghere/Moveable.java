package org.yourorghere;

public abstract class Moveable {

    //�������:
    //���������
    public final Vector location;
    //��������
    public final Vector velocity;
    //���������������
    public final Vector pvelocity;
    //���������������� �����, ��� ���������� ������� ������� � ������ � ����� ����
    public double iMass;

    public Moveable() {
        //�������� ������ ��� �������
        location = new Vector();
        velocity = new Vector();
        pvelocity = new Vector();
    }

}
