import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lowagie.text.DocumentException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class box extends JFrame implements ActionListener {

    String path = ReadJson.class.getClassLoader().getResource("package.json").getPath();
    String json = ReadJson.readJsonFile(path);
    JSONObject jobj = JSON.parseObject(json);

    private JMenuBar menuBar;
    //右键弹出菜单项
    JPopupMenu popupMenu;
    JMenuItem popupMenu_Cut,popupMenu_Copy,popupMenu_Paste,popupMenu_SelectAll;
    //菜单栏
    private JMenu menu_File,menu_Search,menu_Edit,menu_Help;
    //菜单栏内的菜单
    private JMenuItem item_new,item_open,item_save,item_exit,item_print,item_export, item_search;
    //对于file菜单的子项
    private JMenuItem item_selectAll,item_cut,item_copy,item_stick;
    //对于edit菜单的子项
    private JMenuItem item_about;
    //对于help菜单的子项
    private static JTextPane edit_text_pane;
    //编辑区域
    private JScrollPane scroll_bar;
    //可滚动的pane 里面添加edit_text_area就可以变为一个可以滚动的文本框，JScrollPane是一个pane，同时可以设置方向

    public box() {
        initMenuBar();
        initEditArea();
        initListener();
        this.setJMenuBar(menuBar);
        this.setSize((int)jobj.get("width"),(int)jobj.get("height"));
        DateFormat df = DateFormat.getDateTimeInstance();
        String current = df.format(System.currentTimeMillis());
        this.setTitle(jobj.get("title") + current);
        this.add(scroll_bar);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * 两种形式：
     * menu_File.setMnemonic('f'); 对menu
     * item_word_format.setAccelerator(KeyStroke.getKeyStroke('F',java.awt.Event.CTRL_MASK,false)); 对item
     */
    public void initMenuBar() {

        //创建右键弹出菜单
        popupMenu=new JPopupMenu();
        popupMenu_Cut=new JMenuItem("Cut(T)");
        popupMenu_Copy=new JMenuItem("Copy(C)");
        popupMenu_Paste=new JMenuItem("Paste(P)");
        popupMenu_SelectAll=new JMenuItem("SelectAll(A)");


        //向右键菜单添加菜单项和分隔符
        popupMenu.add(popupMenu_Cut);
        popupMenu.add(popupMenu_Copy);
        popupMenu.add(popupMenu_Paste);
        popupMenu.add(popupMenu_SelectAll);

        menuBar = new JMenuBar();
        menu_File = new JMenu("File");
        menu_File.setMnemonic('f');//f+alt打开
        item_new = new JMenuItem("New");
        item_open = new JMenuItem("Open");
        item_save = new JMenuItem("Save");
        item_exit = new JMenuItem("Exit");
        item_print = new JMenuItem("Print");
        item_export = new JMenuItem("Export2PDF");
        menu_File.add(item_new);
        menu_File.add(item_open);
        menu_File.add(item_save);
        menu_File.add(item_print);
        menu_File.add(item_export);
        menu_File.add(item_exit);

        //File 菜单

        menu_Search = new JMenu("Search");
        menu_Search.setMnemonic('s');
        item_search = new JMenuItem("Search");
        menu_Search.add(item_search);
        //Search 菜单

        menu_Edit = new JMenu("Edit");
        menu_Edit.setMnemonic('e');
        item_cut = new JMenuItem("Cut");
        item_copy = new JMenuItem("Copy");
        item_stick = new JMenuItem("Paste");
        item_selectAll = new JMenuItem("SelectAll");
        menu_Edit.add(item_cut);
        menu_Edit.add(item_copy);
        menu_Edit.add(item_stick);
        menu_Edit.add(item_selectAll);
        //Edit 菜单

        menu_Help = new JMenu("Help");
        menu_Help.setMnemonic('h');
        item_about = new JMenuItem("About");
        menu_Help.add(item_about);
        //Help 菜单

        menuBar.add(menu_File);
        menuBar.add(menu_Search);
        menuBar.add(menu_Edit);
        menuBar.add(menu_Help);
    }
    /**
     * 初始化编辑区域
     * 用scrollpane装textarea
     * 同时对pane设置方向
     */
    public void initEditArea() {
        edit_text_pane = new JTextPane();
        edit_text_pane.getDocument().addDocumentListener(new SyntaxHighlighter(edit_text_pane));
        scroll_bar = new JScrollPane(edit_text_pane);
        scroll_bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }


    public void initListener() {
        popupMenu_Cut.addActionListener(this);
        popupMenu_Copy.addActionListener(this);
        popupMenu_Paste.addActionListener(this);
        popupMenu_SelectAll.addActionListener(this);

        //文本编辑区注册右键菜单事件
        edit_text_pane.addMouseListener(new MouseAdapter()
        { public void mousePressed(MouseEvent e)
        { if(e.isPopupTrigger())//返回此鼠标事件是否为该平台的弹出菜单触发事件
        { popupMenu.show(e.getComponent(),e.getX(),e.getY());//在组件调用者的坐标空间中的位置 X、Y 显示弹出菜单
        }
            edit_text_pane.requestFocus();//编辑区获取焦点
        }
            public void mouseReleased(MouseEvent e)
            { if(e.isPopupTrigger())//返回此鼠标事件是否为该平台的弹出菜单触发事件
            { popupMenu.show(e.getComponent(),e.getX(),e.getY());//在组件调用者的坐标空间中的位置 X、Y 显示弹出菜单
            }
                edit_text_pane.requestFocus();//编辑区获取焦点
            }
        });//文本编辑区注册右键菜单事件结束

        item_search.addActionListener(this);
        item_new.addActionListener(this);
        item_open.addActionListener(this);
        item_save.addActionListener(this);
        item_exit.addActionListener(this);
        item_export.addActionListener(this);
        item_print.addActionListener(this);
        item_cut.addActionListener(this);
        item_copy.addActionListener(this);
        item_stick.addActionListener(this);
        item_selectAll.addActionListener(this);
        item_about.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == item_new) {
            new box();
        }else if (e.getSource() == item_exit) {
            this.dispose();
        }else if (e.getSource() == item_open) {
            fileFunction.open(edit_text_pane, scroll_bar);
        }else if (e.getSource() == item_save) {
            fileFunction.save(edit_text_pane, scroll_bar);
        }else if (e.getSource() == item_print) {
            fileFunction.print();
        }else if (e.getSource() == item_export) {
            try {
                fileFunction.export(edit_text_pane, scroll_bar);
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == item_search) {
            search_function.search(this,edit_text_pane);
        }else if (e.getSource() == item_cut) {
            editFunction.cut(edit_text_pane);
        }else if (e.getSource() == item_copy) {
            editFunction.copy(edit_text_pane);
        }else if (e.getSource() == item_stick) {
            editFunction.paste(edit_text_pane);
        }else if (e.getSource() == item_selectAll){
            editFunction.selectAll(edit_text_pane);
        }
        else if (e.getSource() == item_about) {
            new helpFunction.about_Window(jobj);
        }else if (e.getSource() == popupMenu_Cut){
            editFunction.cut(edit_text_pane);
        }else if (e.getSource() == popupMenu_Copy){
            editFunction.cut(edit_text_pane);
        }else if (e.getSource() == popupMenu_Paste){
            editFunction.paste(edit_text_pane);
        }else if (e.getSource() == popupMenu_SelectAll){
            editFunction.selectAll(edit_text_pane);
        }
    }

    public class SyntaxHighlighter implements DocumentListener {
        private Set<String> keywords;
        private Style keywordStyle;
        private Style KeywordStyle2;
        private Style KeywordStyle3;
        private Style KeywordStyle4;
        private Style normalStyle;

        public SyntaxHighlighter(JTextPane editor) {

            // 准备着色使用的样式
            keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
            KeywordStyle2 = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);

            normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
            StyleConstants.setForeground(keywordStyle, Color.orange);
            StyleConstants.setForeground(KeywordStyle2, Color.yellow);
            StyleConstants.setForeground(normalStyle, Color.BLACK);


            // 准备关键字
            keywords = new HashSet<String>();
            JSONArray ja = jobj.getJSONArray("keys");

            for (int i=0;i<ja.size();i++){
                JSONObject jo = ja.getJSONObject(i);
                if(i == 0){
                    Iterator iterator = jo.entrySet().iterator();
                    while(iterator.hasNext()){
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String value = entry.getValue().toString();
                        keywords.add(value);
                    }
                }else if (i == 1){

                }else if (i == 2){

                }
            }

        }

        public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {

            // 取得插入或者删除后影响到的单词.
            // 例如"public"在b后插入一个空格, 就变成了:"pub lic", 这时就有两个单词要处理:"pub"和"lic"
            // 这时要取得的范围是pub中p前面的位置和lic中c后面的位置

            int start = indexOfWordStart(doc, pos);
            int end = indexOfWordEnd(doc, pos + len);

            char ch;
            while (start < end) {
                ch = getCharAt(doc, start);
                if (Character.isLetter(ch) || ch == '_') {
                    // 如果是以字母或者下划线开头, 说明是单词
                    // pos为处理后的最后一个下标
                    start = colouringWord(doc, start);
                } else {
                    SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));

                    ++start;
                }
            }
        }

        /**
         * 对单词进行着色, 并返回单词结束的下标.
         * @param doc
         * @param pos
         * @return
         * @throws BadLocationException
         */
        public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {

            int wordEnd = indexOfWordEnd(doc, pos);
            String word = doc.getText(pos, wordEnd - pos);

            if (keywords.contains(word)) {

                // 如果是关键字, 就进行关键字的着色, 否则使用普通的着色.
                // 这里有一点要注意, 在insertUpdate和removeUpdate的方法调用的过程中, 不能修改doc的属性.
                // 但我们又要达到能够修改doc的属性, 所以把此任务放到这个方法的外面去执行.
                // 实现这一目的, 可以使用新线程, 但放到swing的事件队列里去处理更轻便一点.

                SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
            } else {
                SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
            }
            return wordEnd;
        }

        /**
         * 取得在文档中下标在pos处的字符.
         * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
         * 所以pos的有效值是[0, doc.getLength()]
         * @param doc
         * @param pos
         * @return
         * @throws BadLocationException

         */
        public char getCharAt(Document doc, int pos) throws BadLocationException {

            return doc.getText(pos, 1).charAt(0);
        }

        /**
         * 取得下标为pos时, 它所在的单词开始的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
         * @param doc
         * @param pos
         * @return
         * @throws BadLocationException
         */

        public int indexOfWordStart(Document doc, int pos) throws BadLocationException {

            // 从pos开始向前找到第一个非单词字符.
            for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);
            return pos;
        }

        /**
         * 取得下标为pos时, 它所在的单词结束的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
         * @param doc
         * @param pos
         * @return
         * @throws BadLocationException
         */

        public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {

            // 从pos开始向前找到第一个非单词字符.

            for (; isWordCharacter(doc, pos); ++pos);

            return pos;
        }
        /**
         * 如果一个字符是字母, 数字, 下划线, 则返回true.
         *
         * @param doc
         * @param pos
         * @return
         * @throws BadLocationException
         */

        public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {

            char ch = getCharAt(doc, pos);
            if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
            return false;
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }

        @Override
        public void insertUpdate(DocumentEvent e) {

            try {
                colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                // 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
                colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        /**
         * 完成着色任务
         * @author Biao
         */
        private class ColouringTask implements Runnable {
            private StyledDocument doc;
            private Style style;
            private int pos;
            private int len;

            public ColouringTask(StyledDocument doc, int pos, int len, Style style) {

                this.doc = doc;
                this.pos = pos;
                this.len = len;
                this.style = style;
            }

            public void run() {
                try {
                    // 这里就是对字符进行着色
                    doc.setCharacterAttributes(pos, len, style, true);
                } catch (Exception e) {}
            }
        }
    }

    public static void main(String[] args) {
        box b = new box();
    }

}

