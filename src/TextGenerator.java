import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TextGenerator
{
    /*---------- 문자 클래스, 스타일 enum 선언 ----------*/

    private class ConvertedChar {

        private String[] lines; // 내용

        // 생성자
        public ConvertedChar(String[] lines){

            this.lines = lines;
        }

        // line 번째 줄 반환
        public String getLine(int line) {

            return lines[line];
        }

        // 클래스를 문자열로 변환 시, 줄 순서대로 변환된 문자열 반환
        public String toString() {

            return
                    lines[0] + "\n" +
                    lines[1] + "\n" +
                    lines[2] + "\n" +
                    lines[3] + "\n" +
                    lines[4] + "\n";
        }
    } // 한 문자의 ASCII Art 정보를 담은 클래스

    private enum TextStyle { First, Second, Third } // 문자 스타일
    private enum LineStyle { Horizontal, Vertical } // 출력 스타일 ( 가로, 세로 )
    private enum CapitalStyle { All_Capital, First_Capital, No_Capital }


    /*---------- 컴포넌트 ----------*/

    private JTextField inputField; // 텍스트 입력칸


    private JComboBox styleComboBox;
    private JComboBox capitalStyleComboBox;

    private JButton btnHorizontal, btnVertical;
    private String horizontal = "가로", vertical = "세로";

    private JTextPane outputField; // 텍스트 출력칸

    /*---------- 상수 ----------*/
    private final int height = 5;

    /*---------- 스타일 변수 ----------*/
    private TextStyle textStyle;
    private LineStyle lineStyle;

    /*---------- 생성자 ----------*/
    public TextGenerator()
    {
        /*---------- Frame ----------*/
        JFrame frame = new JFrame("TEXT GENERATOR");
        frame.setLayout(new GridLayout(3,1));
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*---------- Panels ----------*/

        // 패널
        JPanel  settingPanel = new JPanel(),
                inputPanel = new JPanel(),
                outPanel = new JPanel();


        settingPanel.setBorder(new TitledBorder("Settings"));
        inputPanel.setBorder(new TitledBorder("Input"));
        outPanel.setBorder(new TitledBorder("Output"));

        settingPanel.setLayout(new GridLayout(3,1,10,20));
        inputPanel.setLayout(new GridLayout(1,1));
        outPanel.setLayout(new GridLayout(1,1));

        frame.add(settingPanel);
        frame.add(inputPanel);
        frame.add(outPanel);

        /*---------- Components ----------*/

        ActionListener listener = new Action();

        // 문자 스타일 선택
        settingPanel.add(new JLabel("문자 스타일 선택 : "));
        styleComboBox = new JComboBox(TextStyle.values());
        settingPanel.add(styleComboBox);

        // 대소문자 스타일 선택
        settingPanel.add(new JLabel("대/소문자 스타일 선택"));
        capitalStyleComboBox = new JComboBox(CapitalStyle.values());
        settingPanel.add(capitalStyleComboBox);

        // 출력 스타일 선택 : 가로
        btnHorizontal = new JButton(horizontal);
        btnHorizontal.addActionListener(listener);
        settingPanel.add(btnHorizontal);

        // 출력 스타일 선택 : 세로
        btnVertical = new JButton(vertical);
        btnVertical.addActionListener(listener);
        settingPanel.add(btnVertical);

        // 입력칸
        inputPanel.add(new JLabel("Type Something : "));
        inputField = new JTextField(10);
        inputField.addActionListener(listener);
        inputPanel.add(inputField);
        
        // 출력칸
        outputField = new JTextPane();
        outPanel.add(outputField);

        frame.setVisible(true);
    }

    private class Action implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {

            Object source = e.getSource();

            if(source == btnHorizontal)
            {
                SetLineStyle(LineStyle.Horizontal);
            }
            else if(source == btnVertical)
            {
                SetLineStyle(LineStyle.Vertical);
            }

            if(lineStyle != null)
            {
                outputField.setText(""); // 출력 초기화

                String text = inputField.getText(); // 텍스트 입력
                int size = text.length(); // 문자 개수 확인

                ConvertedChar[] input = new ConvertedChar[size]; // 출력할 문자열을 담은 변수 생성, 메모리 할당
                for(int i = 0; i < size; i++) // 입력된 각 문자를 변환하여 input에 대입
                {
                    input[i] = ConvertChar(text.charAt(i),TextStyle.First);
                }
                Write(input, lineStyle); // 완성된 출력 문자열을 textArea에 출력
            }
        }
        // char => ASCII Art 변환
        private ConvertedChar ConvertChar(char character, TextStyle style)
        {
            String result = "성공 :)";

            String[] lines;
            switch(style)
            {
                case First:
                default:
                    switch(character){
                        case 'A': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "        _/_/",
                                                "      _/ _/ ",
                                                "    _/_/_/  ",
                                                "  _/   _/   ",
                                                "_/    _/    "
                                        });
                            
                        case 'B': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "       _/_/ ",
                                                "      _/  _/",
                                                "     _/_/_/ ",
                                                "    _/   _/ ",
                                                "   _/_/_/   "
                                        });
                            
                        case 'C': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "      _/_/  ",
                                                "   _/    _/ ",
                                                "  _/        ",
                                                "  _/    _/  ",
                                                "    _/_/    "
                                        });
                            
                        case 'D': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "      _/_/  ",
                                                "     _/  _/ ",
                                                "    _/   _/ ",
                                                "   _/   _/  ",
                                                "  _/_/_/    "
                                        });
                            
                        case 'E': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "      _/_/_/",
                                                "    _/      ",
                                                "   _/_/_/_/ ",
                                                "  _/        ",
                                                " _/_/_/_/   "
                                        });
                            
                        case 'F': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "      _/_/_/",
                                                "    _/      ",
                                                "   _/_/_/_/ ",
                                                "  _/        ",
                                                " _/         "
                                        });
                            
                        case 'G': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "      _/_/  ",
                                                "   _/    _/ ",
                                                "  _/        ",
                                                "  _/   _/_/ ",
                                                "    _/_/    "
                                        });
                            
                        case 'H': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/    _/",
                                                "   _/    _/ ",
                                                "  _/_/_/_/  ",
                                                " _/    _/   ",
                                                "_/    _/    "
                                        });
                            
                        case 'I': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "     _/_/_/ ",
                                                "      _/    ",
                                                "     _/     ",
                                                "    _/      ",
                                                " _/_/_/     "
                                        });
                            
                        case 'J': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "     _/_/_/ ",
                                                "      _/    ",
                                                "     _/     ",
                                                "_/  _/      ",
                                                " _/_/       "
                                        });
                            
                        case 'K': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "     _/   _/",
                                                "    _/ _/   ",
                                                "   _/_/     ",
                                                "  _/  _/    ",
                                                " _/    _/   "
                                        });
                            
                        case 'L': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/      ",
                                                "   _/       ",
                                                "  _/        ",
                                                " _/         ",
                                                "_/_/_/_/    "
                                        });
                            
                        case 'M': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/      _/",
                                                "   _/_/  _/_/ ",
                                                "  _/  _/  _/  ",
                                                " _/      _/   ",
                                                "_/      _/    "
                                        });
                            
                        case 'N': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/     _/",
                                                "   _/_/   _/ ",
                                                "  _/ _/  _/  ",
                                                " _/  _/ _/   ",
                                                "_/   _/_/    "
                                        });
                            
                        case 'O': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/_/_/  ",
                                                "  _/     _/ ",
                                                " _/      _/ ",
                                                " _/     _/  ",
                                                "  _/_/_/    "
                                        });
                            
                        case 'P': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/_/_/  ",
                                                "   _/     _/",
                                                "  _/_/_/_/  ",
                                                " _/         ",
                                                "_/          "
                                        });
                            
                        case 'Q': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/_/_/  ",
                                                "  _/     _/ ",
                                                " _/      _/ ",
                                                " _/     _/  ",
                                                "  _/_/_/ _/ "
                                        });
                            
                        case 'R': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "   _/_/_/   ",
                                                "   _/     _/",
                                                "  _/_/_/_/  ",
                                                " _/  _/     ",
                                                "_/     _/   "
                                        });
                            
                        case 'S': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/_/_/_/ ",
                                                " _/          ",
                                                "   _/_/_/    ",
                                                "        _/   ",
                                                "_/_/_/_/     "
                                        });
                            
                        case 'T': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "    _/_/_/_/",
                                                "      _/    ",
                                                "     _/     ",
                                                "    _/      ",
                                                "   _/       "
                                        });
                            
                        case 'U': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "  _/      _/",
                                                " _/      _/ ",
                                                "_/      _/  ",
                                                "_/     _/   ",
                                                " _/_/_/     "
                                        });
                            
                        case 'V': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "   _/     _/",
                                                "  _/    _/  ",
                                                " _/   _/    ",
                                                "_/  _/      ",
                                                "_/_/        "
                                        });
                            
                        case 'W': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "   _/       _/",
                                                "  _/       _/ ",
                                                " _/  _/   _/  ",
                                                "_/  _/   _/   ",
                                                "_/_/ _/_/     "
                                        });
                            
                        case 'X': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "     _/     _/",
                                                "     _/   _/  ",
                                                "       _/     ",
                                                "    _/  _/    ",
                                                "  _/     _/   "
                                        });
                            

                        case 'Y': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "     _/     _/",
                                                "    _/    _/  ",
                                                "    _/_/_/    ",
                                                "     _/       ",
                                                "   _/         "
                                        });
                            
                        case 'Z': 
                            Log(character + " 변환 성공 :)");
                            return new ConvertedChar( new String[]
                                        {
                                                "   _/_/_/_/ ",
                                                "       _/   ",
                                                "     _/     ",
                                                "   _/       ",
                                                " _/_/_/_/   "
                                        });
                            
                        default: 
                            Log(character + " 변환 실패 :(");
                            return new ConvertedChar( new String[]
                                        {
                                                "| Error With Character " + character,
                                                "","","",""
                                        });
                    }
                //case Second:
                //case Third:
            }
        }

        private void Write(ConvertedChar[] convertedChars, LineStyle style)
        {
            int charactersCount = convertedChars.length;

            Log("문자 개수: "+charactersCount);
            Log("문자 높이: "+height);
            switch(style)
            {
                case Vertical: // 세로로 출력
                    for(int i = 0; i < convertedChars.length; i++)
                    {
                        Add(convertedChars[i] + "\n");
                    }
                    break;

                default:
                case Horizontal: // 가로로 출력, 기본값
                    for(int h = 0; h < height ; h++) {
                        for (int i = 0; i < charactersCount; i++) {
                            Add(convertedChars[i].getLine(h));
                        }
                        Add("\n");
                    }
                    break;
            }
        }

        private void SetLineStyle(LineStyle style){
            switch(style){

                case Horizontal:
                    lineStyle = LineStyle.Horizontal;
                    btnHorizontal.setEnabled(false);
                    btnVertical.setEnabled(true);
                    break;

                default:
                    lineStyle = LineStyle.Vertical;
                    btnHorizontal.setEnabled(true);
                    btnVertical.setEnabled(false);
                    break;
            }
        }

        private void Add(String line) {

            outputField.setText(outputField.getText() + line);
        }



        private void Log(Object content){
            System.out.println(content);
        }
    }
    public static void main(String[] args)
    {
        new TextGenerator();
    }
}
