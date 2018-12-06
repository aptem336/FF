package org.yourorghere;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Terrain {

    //������ �����, ��� ��������� �����
    private static int mapSize, stepSize;
    //�������� ������ �����
    private static byte[] heightMap;
    //������ ���������� ������������
    public static Triangle[] triangles;

    public static void load(String fileName, int mapSize, int stepSize) {
        Terrain.mapSize = mapSize;
        Terrain.stepSize = stepSize;
        //������ � ������
        heightMap = read(fileName);
        //�������� ������ �������������
        triangles = new Triangle[0];
        //���������� ����� ������������
        for (int x = 0; x < mapSize; x += stepSize) {
            for (int y = 0; y < mapSize; y += stepSize) {
                //�������� ������ ��� ���� �����
                triangles = Arrays.copyOf(triangles, triangles.length + 2);
                //�������� 4 ����������� �����
                Vector[] vertex = getVertexs(x, y);
                //���������� ��� ������������
                // 0 --- 1
                // |  /  |
                // 3 --- 2
                triangles[triangles.length - 2] = new Triangle(new Vector[]{vertex[0], vertex[1], vertex[3]});
                triangles[triangles.length - 1] = new Triangle(new Vector[]{vertex[1], vertex[2], vertex[3]});
            }
        }
    }

    private static Vector[] getVertexs(int x, int y) {
        //���������� 4 ����� �� �������� 
        return new Vector[]{
            new Vector(x - mapSize / 2, getHeight(x, y) - 256, y),
            new Vector(x - mapSize / 2, getHeight(x, y + stepSize) - 256, y + stepSize),
            new Vector(x - mapSize / 2 + stepSize, getHeight(x + stepSize, y + stepSize) - 256, y + stepSize),
            new Vector(x - mapSize / 2 + stepSize, getHeight(x + stepSize, y) - 256, y)};

    }

    private static byte[] read(String fileName) {
        //�������� ������ ��� ������
        byte[] map = new byte[mapSize * mapSize];
        //������ �����
        FileInputStream input;
        try {
            //��������������
            input = new FileInputStream(fileName);
            //������ � ������ mS*mS ����
            input.read(map, 0, mapSize * mapSize);
            input.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    private static double getHeight(int x, int z) {
        //������� x � z ����� ��� �� ����� ���� �� ������� ������� �����
        int mapX = x % mapSize;
        int mapZ = z % mapSize;
        //�������� ������ � ����������� ��������
        return (heightMap[mapX + (mapZ * mapSize)] & 0xFF) * 2d - 512d;
    }

}
