package lv0607;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class Subject {
	private int code;
	private String title;
	private int score;

	// 생성자
	public Subject(int code, String title) {
		this.code = code;
		this.title = title;
	}
	
	// All Argument Constructor 
	public Subject(int code, String title, int score) {
		this.code = code;
		this.title = title;
		this.score = score;
	}

	// getter & setter
	public int getCode() {
		return this.code;
	}

	public String getTitle() {
		return this.title;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}

class Student {
	private int code;
	private String name;

	private int count;
	private Subject[] subjects; // 각 학생이 수강신청한 과목

	public Student(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public int getCount() {
		return this.count;
	}

	// 참조변수의 값을 반환 <- 권장하지 않음
	// ㄴ 참조값을 통해 외부에서 값 변경을 할 수 있기 때문 캡슐화가 무너짐
//	Subject[] getSubjects() {
//		return this.subjects;
//	}

	public Subject[] getSubjects() {
		Subject[] subjects = new Subject[count];
		for (int i = 0; i < count; i++)
			subjects[i] = this.subjects[i];
		return subjects;
	}

//	void setSubjects(Subject[] subjects) {
//		this.subjects = subjects;
//	}

	// 대안으로 사용할 메소드 1
	public void addSubject(Subject subject) {
		Subject[] temp = subjects;
		subjects = new Subject[count + 1];

		for (int i = 0; i < count; i++)
			subjects[i] = temp[i];
		subjects[count++] = subject;
	}

	// 대안으로 사용할 메소드 2
	public void removeSubject(Subject subject) {
		if (!hasSubject(subject))
			return;

		Subject[] temp = subjects;
		subjects = new Subject[count - 1];

		int idx = 0;
		for (int i = 0; i < count; i++) {
			if (subject != temp[i])
				subjects[idx++] = temp[i];
		}
		count--;
	}

	private boolean hasSubject(Subject subject) {
		for (int i = 0; i < count; i++) {
			if (subjects[i] == subject)
				return true;
		}
		return false;
	}

	public Subject getSubjectByIndex(int index) {
		return subjects[index];
	}
	
	public Subject getSubjectByCode(int code) {
		for(int i=0; i<count; i++) {
			if(subjects[i].getCode() == code)
				return subjects[i];
		}
		return null;
	}
	
	public void showSubjectAll() {
		for(int i=0; i<count; i++) {
			System.out.print(i+1 + ") ");
			System.out.println(subjects[i].getTitle());
		}
	}
	
	public double getAverageScore() {
		double average = 0;
		
		double total = 0;
		for(int i=0; i<count; i++) {
			Subject subject = subjects[i];
			int score = subject.getScore();
			total += score;
		}
		average = total / count;
		
		return average;
	}
	
	@Override
	public String toString() {
		String info = String.format("%d번 %s\n", code, name);
		for(int i=0; i<count; i++) {
			Subject subject = subjects[i];
			String str = String.format("ㄴ %d %s : %3d점\n", subject.getCode(), subject.getTitle(), subject.getScore());
		}
		info += "----------";
		return info;
	}
}

class LMS {
	private final int ADD = 1;
	private final int ADD_STUDENT = 1;
	private final int ADD_SUBJECT = 2;
	private final int ADD_SCORE = 3;
	private final int JOIN_SUBJECT = 1;
	private final int OPEN_SUBJECT = 2;
	private final int REMOVE = 2;
	private final int REMOVE_STUDENT = 1;
	private final int REMOVE_SUBJECT = 2;
	private final int ALIGN = 3;
	private final int ALIGN_CODE = 1;
	private final int ALIGN_NAME = 2;
	private final int ALIGN_SCORE = 3;
	private final int VIEW = 4;
	private final int SAVE = 5;
	private final int LOAD = 6;
	private final int EXIT = 0;
	
	private final int STUDENT = 1;
	private final int SUBJECT = 2;

	private Scanner scanner = new Scanner(System.in);
	private Random random = new Random();

	private FileWriter fileWriter;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private File file;

	private String fileName = "lms.txt";

	private String brand;

	private int count;
	private Student[] students;

	private int subjectCnt;
	private Subject[] subjects; // 개설된 과목 관리

	private boolean isRun = true;

	public LMS(String brand) {
		this.brand = brand;
	}

	public void run() {
		while (isRun) {
			printMainMenu();
			selectAndRun();
		}
	}

	private void printMainMenu() {
		System.out.println("1) 추가");
		System.out.println("2) 삭제");
		System.out.println("3) 정렬");
		System.out.println("4) 출력");
		System.out.println("5) 저장");
		System.out.println("6) 로드");
		System.out.println("0) 종료");
	}

	private void selectAndRun() {
		int select = inputNumber("메뉴 선택");

		if (select == ADD)
			add();
		else if (select == REMOVE)
			remove();
		else if (select == ALIGN)
			align();
		else if (select == VIEW)
			view();
		else if (select == SAVE)
			save();
		else if (select == LOAD)
			load();
		else if (select == EXIT) {
			System.out.println("시스템을 종료합니다.");
			isRun = false;
		}
	}

	private void add() {
		System.out.println("1) 학생");
		System.out.println("2) 과목");
		System.out.println("3) 성적");
		int select = inputNumber("항목 선택");

		if (select == ADD_STUDENT)
			addStudent();
		else if (select == ADD_SUBJECT)
			addSubject();
		else if (select == ADD_SCORE)
			addScore();
	}

	private void addStudent() {
		int code = generateRandomCode(STUDENT);
		String name = inputString("학생 이름");
		
		Student student = new Student(code, name);
		
		Student[] temp = students;
		students = new Student[count + 1];
		for(int i=0; i<count; i++)
			students[i] = temp[i];
		students[count ++] = student;
		
		System.out.println("학생 등록이 완료되었습니다.");
	}

	private void addSubject() {
		System.out.println("1) 수강신청");
		System.out.println("2) 과목개설");
		int select = inputNumber("항목 선택");

		if (select == JOIN_SUBJECT)
			joinSubject();
		else if (select == OPEN_SUBJECT)
			openSubject();

	}

	private void joinSubject() {
		// 0. 학번 입력 후 (대상 학생 객체를 얻기),
		int code = inputNumber("학번");
		Student student = getStudentByCode(code);
		
		if(student == null) {
			System.out.println("학번 정보를 다시 확인하세요.");
			return;
		}
		
		// 1. 개설된 과목 목록 제시 
		showSubjectAll();
		
		// 2. 목록 범위 안에서 번호 입력하면 
		int index = inputNumber("신청할 과목 번호") - 1;
		
		if(index < 0 || index >= subjectCnt) {
			System.out.println("유효하지 않은 범위입니다.");
			return;
		}
		
		Subject subject = subjects[index];
		
		// 3. 해당 과목이 중복신청이 아닌지 확인 후,
		if(isDuplSubject(student, subject)) {
			System.out.println("이미 신청한 과목입니다.");
			return;
		}
		// 4. 수강신청 처리 (subject의 복제본)
		int subCode = subject.getCode();
		String subTitle = subject.getTitle();
		Subject target = new Subject(subCode, subTitle);
		student.addSubject(target);
	}
	
	private Student getStudentByCode(int code) {
		for(int i=0; i<count; i++) {
			if(students[i].getCode() == code)
				return students[i];
		}
		return null;
	}

	private boolean isDuplSubject(Student student, Subject subject) {
		// 학생 객체가 보유한 과목 중,
		// 동일한 코드의 -> 과목이 있는지 확인
		int code = subject.getCode();
		Subject target = student.getSubjectByCode(code);
		return target != null;
	}
	
	private void showSubjectAll() {
		for(int i=0; i<subjectCnt; i++) {
			System.out.print(i+1 + ") ");
			System.out.println(subjects[i].getTitle());
		}
	}
	
	private void showStudentAll() {
		for(int i=0; i<count; i++) {
			System.out.print(i+1 + ". ");
			System.out.println(students[i]);
		}
	}

	private void openSubject() {
		String title = inputString("개설할 과목명");
		
		boolean isDupl = false;
		for(int i=0; i<subjectCnt; i++) {
			Subject subject = subjects[i];
			if(title.equals(subject.getTitle()))
				isDupl = true;
		}
		
		if(isDupl) {
			System.out.println("중복된 과목명입니다.");
			return;
		}
		
		Subject[] temp = subjects;
		subjects = new Subject[subjectCnt + 1];
		for(int i=0; i<subjectCnt; i++)
			subjects[i] = temp[i];
		
		int code = generateRandomCode(SUBJECT);
		Subject subject = new Subject(code, title);
		
		subjects[subjectCnt ++] = subject;
	}

	private void addScore() {
		// 1. 학번으로 학생 객체 가져오기
		int code = inputNumber("학번 입력");
		Student student = getStudentByCode(code);
		
		if(student == null) {
			System.out.println("학번 정보를 다시 확인하세요.");
			return;
		}
		
		// 2. 학생이 수강신청한 목록 출력 
		student.showSubjectAll();
		
		// 3. 번호로 대상 과목 선택 후,
		int index = inputNumber("대상 과목 번호") - 1;
		
		if(index < 0 || index >= student.getCount()) {
			System.out.println("유효하지 않은 범위입니다.");
			return;
		}
		
		// 4. 해당 과목의 성적 변경
		Subject subject = student.getSubjectByIndex(index);
		
		int score = inputNumber("성적 입력");
		
		if(score < 0 || score > 100) {
			System.out.println("성적은 1~100까지 입력가능합니다.");
			return;
		}
		subject.setScore(score);
		System.out.println("성적이 반영되었습니다.");
	}

	private void remove() {
		System.out.println("1) 학생");
		System.out.println("2) 과목 (수강철회)");
		int select = inputNumber("항목 선택");

		if (select == REMOVE_STUDENT)
			removeStudent();
		else if (select == REMOVE_SUBJECT)
			removeSubject();

	}

	private void removeStudent() {
		// 1. 학번으로 학생 조회 
		int code = inputNumber("학번 입력");
		Student student = getStudentByCode(code);
		
		// 2. 예외처리 후, 삭제 
		if(student == null) {
			System.out.println("학번 정보를 다시 확인하세요.");
			return;
		}
		
		Student[] temp = students;
		students = new Student[count - 1];
		
		int idx = 0;
		for(int i=0; i<count; i++) {
			if(temp[i] != student)
				students[idx ++] = temp[i];
		}
		count --;
		
		System.out.println("학생 삭제가 완료되었습니다.");
	}

	private void removeSubject() {
		// 수강 철회 
		// 1. 학생 조회 
		int code = inputNumber("학번 입력");
		Student student = getStudentByCode(code);
		
		if(student == null) {
			System.out.println("학번 정보를 다시 확인하세요.");
			return;
		}
		
		// 2. 학생의 수강 목록 출력 
		student.showSubjectAll();
		
		// 3. 과목 번호 입력 
		int index = inputNumber("철회할 과목 번호") - 1;
		
		if(index < 0 || index >= student.getCount()) {
			System.out.println("유효한 범위가 아닙니다. ");
			return;
		}
		
		Subject subject = student.getSubjectByIndex(index);
		
		// 4. 철회 처리 
		student.removeSubject(subject);
		
		System.out.println("수강철회 완료");
	}

	private void align() {
		System.out.println("1) 학번순");
		System.out.println("2) 이름순");
		System.out.println("3) 성적순");
		int select = inputNumber("항목 선택");

		if (select == ALIGN_CODE)
			alignCode();
		else if (select == ALIGN_NAME)
			alignName();
		else if (select == ALIGN_SCORE)
			alignScore();
	}

	private void alignCode() {
		// 정렬(오름차순) 후, 학생 정보 출력
		for(int i=0; i<count; i++) {
			Student first = students[i];
			int index = i;
			
			for(int j=0; j<i; j++) {
				Student target = students[j];
				if(first.getCode() > target.getCode()) {
					first = target;
					index = j;
				}
			}
			
			Student temp = students[i];
			students[i] = first;
			students[index] = temp;
		}
		showStudentAll();
	}

	private void alignName() {
		for(int i=0; i<count; i++) {
			Student first = students[i];
			int index = i;
			
			for(int j=0; j<i; j++) {
				Student target = students[j];
				if(first.getName().compareTo(target.getName()) > 0) {
					first = target;
					index = j;
				}
			}
			
			Student temp = students[i];
			students[i] = first;
			students[index] = temp;
		}
		showStudentAll();
	}

	private void alignScore() {
		// 내림차순 정렬
		for(int i=0; i<count; i++) {
			Student first = students[i];
			int index = i;
			
			for(int j=0; j<i; j++) {
				Student target = students[j];
				if(first.getAverageScore() < target.getAverageScore()) {
					first = target;
					index = j;
				}
			}
			
			Student temp = students[i];
			students[i] = first;
			students[index] = temp;
		}
		showStudentAll();
	}

	private void view() {
		// 1. 학번 입력 후 조회 
		int code = inputNumber("학번 입력");
		Student student = getStudentByCode(code);
		
		if(student == null) {
			System.out.println("학생 정보를 다시 확인하세요.");
			return;
		}
		
		System.out.println(student);
	}

	private void save() {
		String data = getFileDataAsString();
		
		try {
			File file = new File(fileName);
			fileWriter = new FileWriter(file);
			fileWriter.write(data);
			
			System.out.println("파일저장 완료");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("파일저장 실패");
		} finally {
			try {
				fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getFileDataAsString() {
		String data = subjectCnt + "\n";
		
		for(int i=0; i<subjectCnt; i++) {
			Subject subject = subjects[i];
			String info = subject.getCode() + "," + subject.getTitle();
			data += info;
			if(i < subjectCnt - 1)
				data += "/";
		}
		data += "\n";
		
		data += count + "\n";
		for(int i=0; i<count; i++) {
			Student student = students[i];
			String info = student.getCode() + "," + student.getName();
			data += info;
			
			for(int j=0; j<student.getCount(); j++) {
				Subject subject = student.getSubjectByIndex(j);
				info = "/" + subject.getCode() + "," + subject.getScore();
			}
			data += "\n";
		}
		
		return data;
	}

	private void load() {
		// subjectCnt
		// code1,sub1/code2,sub2/code3,sub3...
		// count
		// code1,name1/subCode1,score1/subCode2,score2...
		// code2,name2/subCode1,score1/subCode2,score2...
		// code3,name3/subCode1,score1/subCode2,score2...
		// ...
		
		file = new File(fileName);
		if(file.exists()) {
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				
				// 과목 수 
				String line = bufferedReader.readLine();
				subjectCnt = Integer.parseInt(line);
				
				subjects = new Subject[subjectCnt];
				
				// 과목 목록 
				line = bufferedReader.readLine();
				String[] data = line.split("/");
				
				for(int i=0; i<subjectCnt; i++) {
					String[] info = data[i].split(",");
					
					int code = Integer.parseInt(info[0]);
					String title = info[1];
					Subject subject = new Subject(code, title);
					
					subjects[i] = subject;
				}
				
				// 학생 수 
				line = bufferedReader.readLine();
				count = Integer.parseInt(line);
				
				students = new Student[count];
				
				while(bufferedReader.ready()) {
					line = bufferedReader.readLine();
					
					data = line.split("/");
					
					String[] info = data[0].split(",");
					
					int code = Integer.parseInt(info[0]);
					String name = info[1];
					Student student = new Student(code, name);
					
					Subject[] subjects = new Subject[data.length - 1];
					for(int i=1; i<data.length; i++) {
						info = data[i].split(",");
						
						int subCode = Integer.parseInt(info[0]);
						String title = getSubjectTitleByCode(subCode);
						int score = Integer.parseInt(info[1]);
						
						new Subject(subCode, title, score);
					}
				}
				
			} catch (IOException e) {
			}
		}
	}
	
	private String getSubjectTitleByCode(int code) {
		String title = null;
		for(int i=0; i<subjectCnt; i++) {
			Subject subject = subjects[i];
			if(code == subject.getCode())
				title = subject.getTitle();
		}
		return title;
	}
	
	private int generateRandomCode(int type) {
		if(type == SUBJECT)
			return generateRandomSubjectCode();
		else if(type == STUDENT) 
			return generateRandomStudentCode();
		return 0;
	}
	
	private int generateRandomSubjectCode() {
		while(true) {
			int randomCode = random.nextInt(9000) + 1000;
			
			boolean isDupl = false;
			for(int i=0; i<subjectCnt; i++) {
				Subject subject = subjects[i];
				if(subject.getCode() == randomCode) {
					isDupl = true;
					break;
				}
			}
			
			if(!isDupl)
				return randomCode;
		}
	}
	
	private int generateRandomStudentCode() {
		while(true) {
			int randomCode = random.nextInt(9000) + 1000;
			
			boolean isDupl = false;
			for(int i=0; i<count; i++) {
				Student student = students[i];
				if(student.getCode() == randomCode) {
					isDupl = true;
					break;
				}
			}
			
			if(!isDupl)
				return randomCode;
		}
	}

	private int inputNumber(String msg) {
		System.out.print(msg + " : ");
		String input = scanner.nextLine();

		int number = -1;
		try {
			number = Integer.parseInt(input);
		} catch (Exception e) {
			System.err.println("숫자를 입력하세요.");
		}
		return number;
	}

	private String inputString(String msg) {
		while (true) {
			System.out.print(msg + " : ");
			String input = scanner.nextLine();

			if (!input.equals(""))
				return input;
		}
	}
}

public class Ex01 {
	public static void main(String[] args) {
		LMS system = new LMS("더조은 대학");
		system.run();

	}
}