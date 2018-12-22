package org.yourorghere;

public class ContactJoint {

    //����������� "������������" ����������� ��������
    private static final double ERP = 0.2d;
    //���������� ��������� �������� (���� ��������� <> 0)
    private final double dstVelocity;
    //�������� ��������� ��� ������� �������������
    private final double dstDisVelocity;
    //��������������� ����� ��������������� ���� (��� ����)
    private final double iSumiMass;
    //������� ��� ������� �������� � ������
    private final Vector bounceNormal;
    private final Vector frictionNormal;
    //��� �������������� ����
    private final Ball a, b;
    //��������� ��� � ����������� ���������� => ������
    private final static Ball STATIC_BALL = new Ball(1.0d, 1.0d / 0.0d);

    public ContactJoint(Ball a, Vector normal, double deep) {
        //���� ��� ����� ����, �� ������ �������������� ���������, ����� ���������� ��������� ���� ����������
        this(a, STATIC_BALL, normal, deep);
    }

    public ContactJoint(Ball a, Ball b, Vector normal, double deep) {
        this.a = a;
        this.b = b;
        dstVelocity = Solver.E * (Vector.getDP(a.velocity, normal) - Vector.getDP(b.velocity, normal));
        dstDisVelocity = -ERP * Math.max(0d, deep - 0.05d);
        iSumiMass = 1.0d / (a.iMass + b.iMass);
        this.bounceNormal = normal;
        this.frictionNormal = a.velocity.getNormalized();
    }

    public void solveImpulse() {
        //��������� ������������ ������������ ��������
        double bounce_lambda = calcLambda(a.velocity, b.velocity, bounceNormal, dstVelocity);
        //���� ��� < 0 - ������� ����� ���������
        if (bounce_lambda < 0d) {
            return;
        }
        //����������� �������� ���� �� ������������ ������������
        applyImpulse(a.velocity, b.velocity, bounceNormal, bounce_lambda);
        //��������� �������������� ������������
        double friction_lambda = calcLambda(a.velocity, b.velocity, frictionNormal, 0);
        //������ �� ����� ���� ������ ������� ����� �� ����������� ������
        if (Math.abs(friction_lambda) > (bounce_lambda * Solver.F)) {
            //������� ������ �� ������ ����� � ����������� �����
            friction_lambda = friction_lambda > 0.0f ? 1.0f : -1.0f * bounce_lambda * Solver.F;
        }
        //����������� �������� �� �������������� ������������
        applyImpulse(a.velocity, b.velocity, frictionNormal, friction_lambda);
    }

    public void solvePenetration() {
        //�������������� ������������ "������������"
        double lambda = calcLambda(a.pvelocity, b.pvelocity, bounceNormal, dstDisVelocity);
        //���� ��� < 0 - ������� ����� ���������
        if (lambda < 0d) {
            return;
        }
        //����������� �������������� ���� �� ������������ ������������    
        applyImpulse(a.pvelocity, b.pvelocity, bounceNormal, lambda);
    }

    private double calcLambda(Vector aVel, Vector bVel, Vector normal, double distanceVelocity) {
        //������� ����������� �������
        double dV = 0;
        //�������� ������ �������� �� �������
        dV -= Vector.getDP(aVel, normal);
        dV += Vector.getDP(bVel, normal);
        //�������� ���������
        dV -= distanceVelocity;
        //����� �� 2 �.�. ������� ���
        return dV * iSumiMass;
    }

    //���������� �������� � ���������
    private void applyImpulse(Vector aVel, Vector bVel, Vector normal, double lambda) {
        aVel.add(normal, lambda * a.iMass);
        bVel.add(normal, -lambda * b.iMass);
    }

}
