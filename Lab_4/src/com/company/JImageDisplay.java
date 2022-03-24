package com.company;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

class JImageDisplay extends JComponent {
    private BufferedImage displayImage;

    //конструктор получает на вход длину, ширину
    public JImageDisplay(int width, int height) {
        //иннициализирую объект BufferedImage новым изображением
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //новый объект включённый в пользовательский инитерфейс
        Dimension imageDimension = new Dimension(width, height);
        //вызываем функцию из суперкласса для установки размеров
        super.setPreferredSize(imageDimension);

    }

    //метод просто выводит на экран данное изображение
    @Override
    public void paintComponent(Graphics g) {
        //вызываем функцию из суперкласса
        super.paintComponent(g);
        //рисуем изображение в компоненте
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
    }

    //метод для очистки картинки (все пиксели чёрные)
    public void clearImage() {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    //метод для окраски пикселей
    public void drawPixel(int x, int y, int rgbColor) {
        displayImage.setRGB(x, y, rgbColor);
    }

    //геттер для картинки из другого класса
    public BufferedImage getImage() {
        return displayImage;
    }
}
