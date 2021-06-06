import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/*

2021.05.30 22시

    <변경 내역>
        1. "가로","세로" 모드 전환 함수 삭제, 리스너 이벤트로 이동
        2. 주석 추가
        3. 모든 작업에 콘솔 로그 출력 추가

    <To Do>
        1. 변환 문자열 ■,□ 혹은 고정폭 문자를 사용하여 밀림 현상 해결
        2. GUI 다듬기 ( 대소문자 선택 부분 콤보박스에서 버튼, 체크박스 등으로 교체 고려 )
        3. 소문자 변환 문자열 추가


2021.06.06 21시

    <변경 내역>
        1. ConvertChar() 메서드 안정화
        2. ■,□ 활용하여 출력 문자열 가독성 향상
        3. 출력 문자열 칸 스크롤 막대 추가
        4. 창 아이콘 수정
        
     <To Do>
        1. GUI 디자인 향상
        2. GUI 레이아웃 향상
*/


public class TextGenerator
{
    //*---------- 문자 클래스, 스타일 enum 선언 ----------*/

    // 변환된 단일 문자의 정보를 담은 클래스
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
    }

    // 클립보드에 출력칸 내용 복사
    private class Copy {
        private String str;
        private Toolkit toolkit;
        private Clipboard clipboard;
        private StringSelection select;
        Copy(String str)
        {
            this.str = str;
            toolkit = Toolkit.getDefaultToolkit();
            clipboard = toolkit.getSystemClipboard();
            select = new StringSelection(str);
            clipboard.setContents(select,null);
        }
    }

    // 스타일 열거형
    private enum TextStyle { Upper, Lower } // 문자 스타일 ( 전부 대문자, 전부 소문자 )
    private enum LineStyle { Horizontal, Vertical } // 출력 스타일 ( 가로, 세로 )


    //*---------- 컴포넌트 ----------*/

    private JComboBox styleComboBox; // 스타일 선택 칸 ( 대/소문자 )
    private JButton btnHorizontal, btnVertical; // 가로, 세로 선택 버튼
    private JTextField inputField; // 텍스트 입력칸
    private JTextPane outputField; // 텍스트 출력칸
    private JButton copy; //복사

    //*---------- 상수 ----------*/

    private final int height = 5; // 변환된 문자 하나의 줄 개수 ( for 문에 사용됨 )
    private String horizontal = "가로", vertical = "세로"; // "가로", "세로" 텍스트 ( final 상수는 아니지만 비슷한 역할 )

    //*---------- 스타일 변수 ----------*/

    private TextStyle textStyle; // 대문자, 소문자 사용자 선택을 담은 변수. styleComboBox에 의해 변경.
    private LineStyle lineStyle; // 가로, 세로 사용자 선택을 담은 변수, btnHorizontal, btnVertical에 의해 변경.

    //*---------- 생성자 ----------*/
    public TextGenerator() {
        textStyle = TextStyle.Upper; // 기본값 대문자, styleComboBox에 Listener 추가 후 없애도 됨.

        /*---------- Frame ----------*/
        JFrame frame = new JFrame("TEXT GENERATOR");             // 메인 프레임
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(icon);
        frame.setLayout(new GridLayout(2,1));                  // 레이아웃, 2행 1열
        frame.setSize(600,600);                             // 창의 크기
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // 창이 닫힐 시, 프로그램 종료

        //*---------- Panels ----------*/

        // 패널
        JPanel  settingPanel = new JPanel(), // 제 1행, 설정 패널
                settingPanel1 = new JPanel(), //1행의 1행
                settingPanel2 = new JPanel(), //1행의 2행
                settingPanel3 = new JPanel(), //1행의 3행
                outputPanel = new JPanel(); // 제 2행, 출력 칸 패널


        settingPanel.setBorder(new TitledBorder("Settings")); // 제 1행의 제목
        outputPanel.setBorder(new TitledBorder("Output")); // 제 2행의 제목

        settingPanel.setLayout(new GridLayout(3,1,20, 20)); // 1행 세부설정
        settingPanel1.setLayout(new GridLayout(1,2));
        settingPanel2.setLayout(new GridLayout(1,1));
        settingPanel3.setLayout(new GridLayout(1,2));
        outputPanel.setLayout(new BorderLayout());


        frame.add(settingPanel);    // 1행, 설정 패널을 메인프레임에 추가
        frame.add(outputPanel);      // 2행, 입력 패널을 메인프레임에 추가

        //settingPanel에 패널3개 추가
        settingPanel.add(settingPanel1);
        settingPanel.add(settingPanel2);
        settingPanel.add(settingPanel3);

        /*---------- Components ----------*/

        ActionListener listener = new Action(); // 리스너 클래스인 Action 클래스를 listener 변수에 할당. 이후 add(listener) 사용

        // 문자 스타일 선택
        settingPanel1.add(new JLabel("대/소문자 선택 : ")); // 패널에 라벨 추가
        styleComboBox = new JComboBox(TextStyle.values());  // 콤보박스의 선택지, ( Upper, Lower )
        styleComboBox.addActionListener(listener);
        settingPanel1.add(styleComboBox); // 패널에 대,소문자 선택 콤보박스 추가

        // 입력칸
        inputField = new JTextField(1); // 입력칸
        inputField.addActionListener(listener); // 리스너 추가 ( 사용자 엔터 입력 시, 변환을 진행해야 하므로 )
        settingPanel2.add(inputField); // 패널에 추가

        // 출력 스타일 선택 : 가로
        btnHorizontal = new JButton(horizontal); // "가로"로 설정하는 버튼
        btnHorizontal.addActionListener(listener); // 리스너 추가
        settingPanel3.add(btnHorizontal); // 패널에 추가

        // 출력 스타일 선택 : 세로
        btnVertical = new JButton(vertical); //"세로"로 설정하는 버튼
        btnVertical.addActionListener(listener); // 리스너 추가
        settingPanel3.add(btnVertical); // 패널에 추가


        // 출력칸
        outputField = new JTextPane(); // 출력칸
        JScrollPane scrollPane = new JScrollPane(outputField);
        outputPanel.add(scrollPane, BorderLayout.CENTER); // 패널에 추가


        copy = new JButton("COPY");//복사버튼
        copy.addActionListener(listener);

        outputPanel.add(copy, BorderLayout.SOUTH);




        frame.setVisible(true); // 노출
    }

    private class Action implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {

            Object source = e.getSource();

            //=== 버튼, 콤보박스 ===//
            if(source == btnHorizontal) // "가로" 버튼을 조작했을 경우에, "가로" 모드로 변경
            {
                lineStyle = LineStyle.Horizontal; // lineStyle 변수에 "가로" 할당
                btnHorizontal.setEnabled(false);  // "가로" 모드로 변경했으므로, "가로" 모드 버튼 비활성화
                btnVertical.setEnabled(true);     // "가로" 모드로 변경했으므로, "세로" 모드 버튼 활성화
            }

            else if(source == btnVertical) // "세로" 버튼을 조작했을 경우에, "세로" 모드로 변경
            {
                lineStyle = LineStyle.Vertical;   // lineStyle 변수에 "세로" 할당
                btnHorizontal.setEnabled(true);   // "세로" 모드로 변경했으므로, "가로" 모드 버튼 활성화
                btnVertical.setEnabled(false);    // "세로" 모드로 변경했으므로, "세로" 모드 버튼 비활성화
            }

            else if(source == styleComboBox) // 대,소문자 콤보박스
            {
                if(styleComboBox.getSelectedItem().toString() == "Upper")
                {
                    textStyle = TextStyle.Upper;
                }
                else
                {
                    textStyle = TextStyle.Lower;
                }

            }
            else if(source == copy)
            {
                new Copy(outputField.getText());
            }


            //=== 문자열 출력 ===//
            if(lineStyle != null) // "가로","세로" 모드가 정의되어 있지 않을 경우, 출력하지 않음. "대문자","소문자" 모드 null체크도 필요할 예정.
            {
                outputField.setText(""); // 출력칸 초기화

                String text = inputField.getText(); // 입력칸의 텍스트를 text 변수에 할당
                int size = text.length(); // 문자 개수 확인

                // ------ 대,소문자 설정 ------ //

                switch (textStyle) // 대,소문자 모드 확인
                {
                    default:
                    case Upper:                                 // 대문자 모드일 경우에,
                        text = text.toUpperCase(Locale.ROOT);   // 모두 대문자로 변경
                        break;
                    case Lower:                                 // 소문자 모드일 경우에,
                        text = text.toLowerCase(Locale.ROOT);   // 모두 소문자로 변경
                        break;
                }



                // ------ 변환 ------ //

                ConvertedChar[] input = new ConvertedChar[size]; // 출력할 문자열을 담은 변수 생성, 메모리 할당

                Log("\n/ ------------ 변환 시작 ------------- /\n");
                Log("변환 문자열 : " + text);
                for(int i = 0; i < size; i++) // 입력된 각 문자를 변환하여 input에 대입
                {
                    input[i] = ConvertChar(text.charAt(i)); // 변환 작업
                }
                Log("\n/ ------------ 변환 완료 ------------- /\n");





                // ------ 출력 ------ //

                Write(input, lineStyle); // 완성된 출력 문자열을 textArea에 출력
            }
        }

        // char => ASCII Art 변환, 입력받은 문자를 바탕으로 ConvertedChar의 생성자를 호출하여 반환
        private ConvertedChar ConvertChar(char character) {
            Log("[변환] 문자 " + character + " 변환 시도 중 ...");
            switch(character){
                case 'A':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■□□",
                                    "□■□□□■□",
                                    "□■■■■■□",
                                    "□■□□□■□",
                                    "□■□□□■□"
                            });

                case 'B':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■□□",
                                    "□■□□□■□",
                                    "□■■■■□□",
                                    "□■□□□■□",
                                    "□■■■■□□"
                            });

                case 'C':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■□□",
                                    "□■□□□■□",
                                    "□■□□□□□",
                                    "□■□□□■□",
                                    "□□■■■□□"
                            });

                case 'D':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■□□□",
                                    "□■□□■□□",
                                    "□■□□□■□",
                                    "□■□□■□□",
                                    "□■■■□□□"
                            });

                case 'E':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■□",
                                    "□■□□□□□",
                                    "□■■■■■□",
                                    "□■□□□□□",
                                    "□■■■■■□"
                            });

                case 'F':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■□",
                                    "□■□□□□□",
                                    "□■■■■■□",
                                    "□■□□□□□",
                                    "□■□□□□□"
                            });

                case 'G':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■□□",
                                    "□■□□□■□",
                                    "□■□□□□□",
                                    "□■□□■■■",
                                    "□□■■■□□"
                            });

                case 'H':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□■■■■■□",
                                    "□■□□□■□",
                                    "□■□□□■□"
                            });

                case 'I':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■□",
                                    "□□□■□□□",
                                    "□□□■□□□",
                                    "□□□■□□□",
                                    "□■■■■■□"
                            });

                case 'J':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■□",
                                    "□□□■□□□",
                                    "□□□■□□□",
                                    "□■□■□□□",
                                    "□■■■□□□"
                            });

                case 'K':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□■□",
                                    "□■□□■□□",
                                    "□■■■□□□",
                                    "□■□□■□□",
                                    "□■□□□■□"
                            });

                case 'L':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□□□",
                                    "□■□□□□□",
                                    "□■□□□□□",
                                    "□■□□□□□",
                                    "□■■■■■□"
                            });

                case 'M':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■□□□■■□",
                                    "□■□■□■□■□",
                                    "□■□□■□□■□",
                                    "□■□□□□□■□",
                                    "□■□□□□□■□"
                            });

                case 'N':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■□□□□■□",
                                    "□■□■□□□■□",
                                    "□■□□■□□■□",
                                    "□■□□□■□■□",
                                    "□■□□□□□■□"
                            });

                case 'O':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■□□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□□■■■□□"
                            });

                case 'P':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■□□",
                                    "□■□□□■□",
                                    "□■■■■□□",
                                    "□■□□□□□",
                                    "□■□□□□□"
                            });

                case 'Q':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■□□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□□■■■□■"
                            });

                case 'R':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■□□",
                                    "□■□□□■□",
                                    "□■■■■□□",
                                    "□■□□■□□",
                                    "□■□□□■□"
                            });

                case 'S':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■■■□",
                                    "□■□□□□□",
                                    "□□■■■□□",
                                    "□□□□□■□",
                                    "□■■■■□□"
                            });

                case 'T':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■□",
                                    "□□□■□□□",
                                    "□□□■□□□",
                                    "□□□■□□□",
                                    "□□□■□□□"
                            });

                case 'U':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□■□□□■□",
                                    "□□■■■□□"
                            });

                case 'V':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□□□□□■□",
                                    "□□■□□□□□■□□",
                                    "□□□■□□□■□□□",
                                    "□□□□■□■□□□□",
                                    "□□□□□■□□□□□"
                            });

                case 'W':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□■□□□■□",
                                    "□■□□□■□□□■□",
                                    "□■□□□■□□□■□",
                                    "□■□□□■□□□■□",
                                    "□□■■■□■■■□□"
                            });

                case 'X':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□□□■□",
                                    "□□■□□□■□□",
                                    "□□□■■□□□□",
                                    "□□■□□□■□□",
                                    "□■□□□□□■□"
                            });


                case 'Y':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□□■□",
                                    "□□■□□■□□",
                                    "□□□■□□□□",
                                    "□□□■□□□□",
                                    "□□□■□□□□"
                            });

                case 'Z':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■■■■■□",
                                    "□□□□□□■□□",
                                    "□□□■■□□□□",
                                    "□□■□□□□□□",
                                    "□■■■■■■■□"
                            });

                case 'a':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■□□",
                                    "■□□■□",
                                    "■□□■□",
                                    "□■■□■",
                                    "□□□□□"
                            });
                case 'b':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□",
                                    "□■□□□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□"
                            });
                case 'c':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■■□",
                                    "□■□□□",
                                    "□■□□□",
                                    "□□■■□",
                                    "□□□□□"
                            });
                case 'd':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□■□",
                                    "□□□■□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□"
                            });
                case 'e':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■□□",
                                    "□■□■□",
                                    "□■■■□",
                                    "□■□□□",
                                    "□□■■□"
                            });
                case 'f':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□■□",
                                    "□□■□■",
                                    "□■■■□",
                                    "□□■□□",
                                    "□□■□□"
                            });
                case 'g':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□",
                                    "□□□■□",
                                    "□■■□□"
                            });
                case 'h':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□",
                                    "□■□□□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■□■□"
                            });
                case 'i':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■□□",
                                    "□□□□□",
                                    "□□■□□",
                                    "□□■□□",
                                    "□□■□□"
                            });
                case 'j':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■□□",
                                    "□□□□□",
                                    "□□■□□",
                                    "■□■□□",
                                    "□■□□□"
                            });
                case 'k':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□■□□□",
                                    "□■□■□",
                                    "□■■□□",
                                    "□■□■□",
                                    "□□□□□"

                            });
                case 'l':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□■□□",
                                    "□□■□□",
                                    "□□■□□",
                                    "□□■□□",
                                    "□□■□□"
                            });
                case 'm':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■□■□",
                                    "■□■□■",
                                    "■□■□■",
                                    "□□□□□"
                            });


                case 'n':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□w□□□",
                                    "□■■w□",
                                    "□■□■□",
                                    "□■□■□",
                                    "□aa□□"
                            });

                case 'o':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□",
                                    "□□□□□"
                            });

                case 'p':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□",
                                    "□■□□□"
                            });

                case 'q':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■■■□",
                                    "□■□■□",
                                    "□■■■□",
                                    "□□□■□"
                            });

                case 'r':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■□■□",
                                    "□■■□□",
                                    "□■□□□",
                                    "□■□□□"
                            });

                case 's':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□■■□",
                                    "□■■□□",
                                    "□□□■□",
                                    "□■■□□"
                            });

                case 't':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□■□□",
                                    "□■■■□",
                                    "□□■□□",
                                    "□□■□□"
                            });

                case 'u':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□□□□",
                                    "□■□■□",
                                    "□■□■□",
                                    "□■■■□"
                            });

                case 'w':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□□□□",
                                    "■□■□■",
                                    "■□■□■",
                                    "□■□■□"
                            });

                case 'x':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□□□□",
                                    "□■□■□",
                                    "□□■□□",
                                    "□■□■□"
                            });
                case 'y':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□□□□□",
                                    "□■□■□",
                                    "□□■□□",
                                    "□□■□□"
                            });
                case 'z':
                    Log("[변환] 문자 " + character + " 변환 성공 :)");
                    return new ConvertedChar( new String[]
                            {
                                    "□□□□□",
                                    "□■■■□",
                                    "□□□■□",
                                    "□□■□□",
                                    "□■■■□"
                            });
                default:
                    Log("[변환] 문자 " + character + " 변환 실패 :(");
                    return new ConvertedChar( new String[]
                            {
                                    "| Error With Character " + character,
                                    "","","",""
                            });
            }


        }

        // 출력
        private void Write(ConvertedChar[] convertedChars, LineStyle style) {
            Log("\n/ ------------ 출력 시작 ------------- /\n");
            int charactersCount = convertedChars.length;
            Log("[출력] 출력할 문자 개수: " + charactersCount + ", " + style + " 모드\n");
            switch(style)
            {
                case Vertical: // 세로로 출력 (위에서 아래로 하나씩 나열)
                    for(int i = 0; i < convertedChars.length; i++)
                    {
                        Add(convertedChars[i] + "□□□□□□□\n"); // ConvertedChar 클래스를 통째로 문자열로 변환할 경우, 자동으로 변환된 문자열 반환
                        Log("[출력 : 세로] " + (i+1) + "번째 문자 세로로 출력 완료.");
                    }
                    break;

                default:
                case Horizontal: // 가로로 출력, 기본값
                    for(int h = 0; h < height ; h++) {
                        for (int i = 0; i < charactersCount; i++) {
                            Add(convertedChars[i].getLine(h));
                            Log("[출력 : 가로] " + i +"번째 문자의 " + h + "번째 줄 출력 완료.");
                        }
                        Add("\n");
                        Log("[출력 : 가로] 모든 문자의 " + h + "번째 줄 출력 완료, 줄 변경.\n");
                    }
                    break;
            }
            Log("\n/ ------------ 출력 완료 ------------- /\n");
        }

        // 출력칸에 문자열 추가 ( 줄바꿈 없음 )
        private void Add(String line) {
            outputField.setText(outputField.getText() + line);
        }


        // 콘솔에 로그 출력을 위한 함수
        private void Log(Object content){
            System.out.println(content);
        }
    }
    public static void main(String[] args)
    {
        new TextGenerator();
    }
}
