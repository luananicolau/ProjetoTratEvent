import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TodoList extends JFrame {
    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton addButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JButton deleteButton;
    private JButton markDoneButton;
    private JComboBox<String> filterComboBox;
    private JButton clearCompletedButton;

    private List<Task> tasks;

    public TodoList() {
        super("To-Do List App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setVisible(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        taskInputField = new JTextField();
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Excluir");
        markDoneButton = new JButton("Concluir");
        filterComboBox = new JComboBox<>(new String[] { "Todas", "Ativas", "Concluídas" });
        clearCompletedButton = new JButton("Limpar Concluídas");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
        inputPanel.add(taskInputField);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(deleteButton);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(filterComboBox);
        buttonPanel.add(clearCompletedButton);

        mainPanel.add(inputPanel);
        mainPanel.add(new JScrollPane(taskList));
        mainPanel.add(buttonPanel);
        add(mainPanel);

        // Tratamento de Eventos
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "A tarefa foi Excluída");
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    tasks.remove(selectedIndex);
                    updateTaskList();
                }
            }
        });

        markDoneButton.setBackground(Color.GREEN);
        markDoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "A tarefa foi Concluída");
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Task selectedTask = tasks.get(selectedIndex);
                    selectedTask.setDone(true);
                    updateTaskList();
                }
            }
        });

        filterComboBox.setBackground(Color.BLUE);
        filterComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedFilter = (String) filterComboBox.getSelectedItem();
                switch (selectedFilter) {
                    case "Todas":
                        updateTaskList(tasks);
                        break;
                    case "Ativas":
                        List<Task> tasksAtivas = new ArrayList<>();
                        for (Task task : tasks) {
                            if (!task.isDone()) {
                                tasksAtivas.add(task);
                            }
                        }
                        updateTaskList(tasksAtivas);
                        break;
                    case "Concluídas":
                        List<Task> tasksConcluidas = new ArrayList<>();
                        for (Task task : tasks) {
                            if (task.isDone()) {
                                tasksConcluidas.add(task);
                            }
                        }
                        updateTaskList(tasksConcluidas);
                        break;
                }
            }

            private void updateTaskList(List<Task> tasksToShow) {
                listModel.clear();
                for (Task task : tasksToShow) {
                    listModel.addElement(task.getDescription());
                }
            }
        });

        clearCompletedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Task> tarefasAtivas = new ArrayList<>();
                for (Task task : tasks) {
                    if (!task.isDone()) {
                        tarefasAtivas.add(task);
                    }
                }
                tasks = tarefasAtivas;
                updateTaskList(tasks);
            }

            private void updateTaskList(List<Task> tasksToShow) {
                listModel.clear();
                for (Task task : tasksToShow) {
                    listModel.addElement(task.getDescription());
                }
            }
        });

        taskInputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask();
                }
            }
        });
    }

    private void addTask() {
        String taskDescription = taskInputField.getText().trim();
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            tasks.add(newTask);
            updateTaskList();
            taskInputField.setText("");
            JOptionPane.showMessageDialog(null, "A tarefa foi adicionada");
        }
    }

    private void updateTaskList() {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription());
        }
    }
}
