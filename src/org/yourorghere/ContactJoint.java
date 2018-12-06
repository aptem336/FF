package org.yourorghere;

public class ContactJoint {

    //����������� "������������" ����������� ��������
    private static final double ERP = 0.2d;
    //���������� ��������� �������� (���� ��������� <> 0)
    private final double dstVelocity;
    //�������� ��������� ��� ������� �������������
    private final double dstDisVelocity;
    //������� ��� ������� �������� � ������
    private final Vector bounceNormal, frictionNormal;

    public ContactJoint(Vector normal, double deep) {
        //�������� �������
        dstVelocity = Solver.E * Vector.getDP(Solver.BALL.velocity, normal);
        //�������� "������������"
        dstDisVelocity = -ERP * Math.max(0d, deep - 0.05d);
        this.bounceNormal = normal;
        //������� ������ - �������������� ��������
        this.frictionNormal = Solver.BALL.velocity.getNormalized();
    }

    public void solveImpulse() {
        //��������� ������������ ������������ ��������
        double blambda = calcLambda(Solver.BALL.velocity, bounceNormal, dstVelocity);
        //���� ��� < 0 - ������� ����� ���������
        if (blambda < 0d) {
            return;
        }
        //����������� �������� ���� �� ������������ ������������
        applyImpulse(Solver.BALL.velocity, bounceNormal, blambda);
        //��������� �������������� ������������
        double flambda = calcLambda(Solver.BALL.velocity, frictionNormal, 0);
        //������ �� ����� ���� ������ ������� ����� �� ����������� ������
        if (Math.abs(flambda) > (blambda * Solver.F)) {
            //������� ������ �� ������ ����� � ����������� �����
            flambda = flambda > 0.0f ? 1.0f : -1.0f * blambda * Solver.F;
        }
        //����������� �������� �� �������������� ������������
        applyImpulse(Solver.BALL.velocity, frictionNormal, flambda);
    }

    public void solvePenetration() {
        //�������������� ������������ "������������"
        double lambda = calcLambda(Solver.BALL.pvelocity, bounceNormal, dstDisVelocity);
        //���� ��� < 0 - ������� ����� ���������
        if (lambda < 0d) {
            return;
        }
        //����������� �������������� ���� �� ������������ ������������    
        applyImpulse(Solver.BALL.pvelocity, bounceNormal, lambda);
    }

    private double calcLambda(Vector vel, Vector normal, double dstV) {
        //������� ����������� �������
        double dV = 0;
        //�������� �������� �� �������
        dV -= Vector.getDP(vel, normal);
        //�������� ���������
        dV -= dstV;
        return dV;
    }

    //���������� �������� � ���������
    private void applyImpulse(Vector vel, Vector normal, double lambda) {
        vel.add(normal, lambda);
    }

}
