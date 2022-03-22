package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

public class FractalExplorer
{
    //размер дисплея
    private int displaySize;

    //ссылка для обнолвления дисплей после измерения фрактала
    private JImageDisplay display;

    //объект для генератора фракталов
    private FractalGenerator fractal;

    //объект который указывает диапозон, который мы сейчас показываем
    private Rectangle2D.Double range;

    //конструктор для установки размера дисплея и инициализаии объектов для генератора фрактала
    public FractalExplorer(int size) {
        //запоминаю размер дисплея
        displaySize = size;

        //инициализирую генератор фрактала и объекты
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    //создаём GUI(графикал юзер интерфейс)
    public void createAndShowGUI()
    {
        //устанавливаем рамку
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        //добавляем место для картинки и центрируем его
        myFrame.add(display, BorderLayout.CENTER);

        //создаём кнопку ресет (возвращает картинку в исходное состояния)
        JButton resetButton = new JButton("Reset");

        //экземляр кнопки сброса и добавление ему активности
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        //экземпляр клика мышкой по фракталу и добавление ему активности
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        //крестик - закрыть и остановить программу
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //создаю верхнеюю панель, добавляю лейбл(место для текста) и устанавливаю е   сверху
        JPanel myTopPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myTopPanel.add(myLabel);
        myFrame.add(myTopPanel, BorderLayout.NORTH);

        //создаю нижнюю панель и добавляю на неё кнопку ресета
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);


        //располагаю содержимое на рамке, делаю это видиым и запрещаю изменять размер окна
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    //метод для рисования фрактала
    private void drawFractal() {
        //проходим через каждый пиксель на доске
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                //находим соответствующие координаты х и у в фрактальной зоне
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                //считаем количество итерация для координаты в фрактальной зоне
                int iteration = fractal.numIterations(xCoord, yCoord);

                //если колличество итераций -1, то в чёрный цвет
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }

                else {

                    //если не -1, то выбираем цвет на основе количества итераий
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    //добавляем цвета на дисплей
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        //когд все пиксели добавлены, перерисовываем дисплей
        display.repaint();
    }

    //метод для определения действия при нажатии на кнопки
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //получаю источник действия
            String command = e.getActionCommand();

            if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            else return;
        }
    }

    //класс для клика мышкой
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            //получаю координаты клика
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            //вызываю метод для зума, который делает зум в 2 раза к нужным координатам
            //и таким образом делает изменённый фрактал
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            //перерисовываю фрактал
            drawFractal();
        }
    }

    //метод мейн для вызова и рисования фрактала
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
