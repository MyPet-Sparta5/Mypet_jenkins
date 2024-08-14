![header](https://capsule-render.vercel.app/api?type=slice&color=FFF9EB&height=100&section=header&text=😸나만,펫%20프로젝트😸&fontSize=60&fontAlignY=55)

## 목차
- [👨🏻‍👩🏻‍👧🏻‍👦🏻 Team 소개](#team)
- [🎨 Teck Stack](#teck-stack)
- [🎯 구현기능](#feature)
- [📑 Technical Documentation](#tech-doc)
- [⚠️ 트러블 슈팅](#trouble)
<br>

<div id="team">
   
# 👨🏻‍👩🏻‍👧🏻‍👦🏻 PetZoa 팀 소개

   
| 김세림                         | 이가은                         | 김승수                         | 박민혁                         | 이윤성                         |
|-------------------------------|-------------------------------|-------------------------------|-------------------------------|-------------------------------|
| [![김세림](https://github.com/serim01.png)](https://github.com/serim01) | [![이가은](https://github.com/GaEun1216.png)]([https://github.com/GaEun1216]) | [![김승수](https://github.com/seungsuuu.png)](https://github.com/seungsuuu) | [![박민혁](https://github.com/hanraeul.png)](https://github.com/hanraeul) | [![이윤성](https://github.com/lis0517.png)](https://github.com/lis0517) |

<details>
<summary>김세림</summary>
<div markdown="1">

- 백오피스 기능
  - 게시물 관리(조회, 검색기능, 상태변경)
  - 유저 관리(조회, 검색기능, 상태변경, 역할변경)
  - 신고 관리(조회, 검색기능, 상태변경)
  - 정지 관리(조회, 검색기능)
- 프론트엔드: 전반적인 화면 구성 / 백오피스 연동

</div>
</details>

<details>
<summary>이가은</summary>
<div markdown="1">

- 게시물 RD
- AWS S3 업로드
- 사진, 동영상 압축기술( Imgscalr, FFmpeg )
- 프론트엔드 : 일부 세부 화면 구성 / 포스트 상세페이지, 신고기능 연동

</div>
</details>

<details>
<summary>김승수</summary>
<div markdown="1">

- 인증 및 인가 (security)
- 마이페이지
- 신고하기 기능
- 프론트엔드 : 로그인, 회원가입, 마이페이지 연동

</div>
</details>

<details>
<summary>박민혁</summary>
<div markdown="1">

- 게시물CU
- 파일 업로드 기능
- CI/CD
- 프론트엔드 : 게시판 목록, 게시물 작성 연동

</div>
</details>

<details>
<summary>이윤성</summary>
<div markdown="1">

- 댓글, 좋아요 기능
- 동물병원 데이터 추가 및 범위 검색
- 소셜로그인 (구글, 카카오)
- 이메일 인증
- 프론트엔드: 댓글, 좋아요 연동 / 카카오 Map api 연동

</div>
</details>
</div>

<div id="teck-stack">
  
# 🎨 Teck Stack

### Infra
* <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white">
* <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
* <img src="https://img.shields.io/badge/aws loadbalancing-8C4FFF?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white">
* <img src="https://img.shields.io/badge/Amazon s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
* <img src="https://img.shields.io/badge/Route 53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white">
* <img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"><img src="https://img.shields.io/badge/1.24.0-515151?style=for-the-badge">
* <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/27.1.1-515151?style=for-the-badge">
* <img src="https://img.shields.io/badge/Docker Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/2.29.1-515151?style=for-the-badge">
---
### Backend
* <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
* <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"><img src="https://img.shields.io/badge/17-515151?style=for-the-badge">
* <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.3.0-515151?style=for-the-badge">
* <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
* <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/8.0.39-515151?style=for-the-badge">
* <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/7.2.0-515151?style=for-the-badge">
---
### Frontend
* ![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white) 
* ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
---
### Document
* ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)  
* ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
* <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
* <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
* ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
* ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

</div>

<div id = "feature">
  
# 🎯 구현기능

<details> 
  <summary>사용자 기능 </summary>
<div markdown="1">

- [x] 로그인 기능
- [x] 로그아웃 기능
- [x] 회원가입
  - [x] 이메일 인증 
- [x] 소셜로그인
- [x] 마이페이지
  - [x] 비밀번호 변경
  - [x] 닉네임 변경
  - [x] 회원 탈퇴
  - [x] 소셜 연동
</div>
</details>

<details> 
  <summary>게시판 기능 </summary>
<div markdown="1">

- [x] 게시물 생성 (자랑하기 게시판은 이미지 업로드 가능)
- [x] 게시물 삭제
- [x] 게시물 수정
- [x] 게시물 조회

</div>
</details>

<details> 
  <summary>댓글 기능 </summary>
<div markdown="1">

- [x] 댓글 생성
- [x] 댓글 삭제
- [x] 댓글 조회
</div>
</details>

<details> 
  <summary>좋아요 기능 </summary>
<div markdown="1">

- [x] 좋아요 조회
- [x] 좋아요 생성
- [x] 좋아요 삭제
</div>
</details>

<details> 
  <summary>백오피스 기능 </summary>
<div markdown="1">

- [x] 게시물 관리
  - [x] 게시물 조회 (카테고리, 상태, 제목, 작성자, 기간조회로 검색도 가능)
  - [x] 게시물 상태 변경
- [x] 회원 관리
  - [x] 회원 조회 (이메일, 상태, 역할로 검색도 가능)
  - [x] 회원 역할 변경
  - [x] 회원 상태 변경
- [x] 신고 관리
  - [x] 신고 조회 (이메일, 상태, 기간조회로 검색도 가능)
  - [x] 신고 상태 변경 
- [x] 회원 정지 관리
  - [x] 정지 목록 조회 (이메일, 기간조회로 검색도 가능)

</div>
</details>

<details> 
  <summary>펫 서비스 시설 기능 </summary>
<div markdown="1">

- [x] 펫 서비스 시설 조회

</div>
</details>

<div id = "tech-doc">
  
# 📑 Technical Documentation

<details>
<summary>⭐와이어 프레임</summary>
<div markdown="1">

![image](https://github.com/user-attachments/assets/66abcbcc-5196-4510-80ca-7b87c1a3a189)


</div>
</details>

<details>
<summary>🧬 ERD DIAGRAM</summary>
<div markdown="1">
 
   ![스크린샷 2024-08-12 181631](https://github.com/user-attachments/assets/ed623075-ee25-4614-89cf-64de0e926526)


</div>
</details>

<details>
<summary> 🔨 API 명세서</summary>
<div markdown="1">
  


</div>
</details>

<details>
<summary>🔱 Branch Rule</summary>
<div markdown="1">
  
## 🔱  Branch Rule
- main, dev, feature 브랜치 사용.
- feature 브랜치에서 기능 개발 완료시 dev 브랜치로 merge
- 프로젝트 완료시 main 브랜치로 merge
- **feat/#이슈번호-이슈내용**
> ex)  
> feature/#1-post-create


</div>
</details>

</div>
</details>


<details>
<summary>🌠 Commit Rule</summary>
<div markdown="1">
  
## 🌠 Commit Rule
- **[#이슈번호] '작업 타입' : '작업 내용'**
> ex)  
> [#36] ✨ feat : 회원가입 기능 추가

| 작업 타입 | 작업내용 |
| --- | --- |
| ✨ feat | 새로운 기능을 추가 |
| 🐛 bugfix | 버그 수정 |
| ♻️ refactor | 코드 리팩토링 |
| 🩹 fix | 코드 수정 |
| 🚚 move | 파일 옮김/정리 |
| 🔥 del | 기능/파일을 삭제 |
| 🍻 test | 테스트 코드를 작성 |
| 💄 style | css 수정 |
| 🙈 github | 깃허브내 수정 |
| 🔨 script | package.json 변경(npm 설치 등) |
| 🔧 chore | 코드 수정 없이 설정 파일만 수정 |


</div>
</details>



<details>
<summary>🚀 Code Convention</summary>
<div markdown="1">
  
## 🚀 Code Convention

### 네이버 코드 컨벤션 적용
    - `Intelli J` - `Setting` - `Editor` - `Code Style`

### Check Style
    - `SonarLint plugin` 사용

### 약어 금지

### 클래스는 `Pascal Case`

### 함수, 변수는 `Camel Case`

### 상수는 `Scream Snack Case`

### 빌더 패턴 사용하기

</div>
</details>
<div id ="trouble">
  
# ⚠️ 트러블 슈팅

<details>
<summary>Cors 문제 </summary>
<div markdown="1">
   
1. local 환경 (백앤드 → 프론트 통신)
    - 백앤드에서 `Authorization` Header 값을 프론트로 전달하지 못하는 문제
        
        ![image (6)](https://github.com/user-attachments/assets/29ca4193-2611-46f7-97ab-7f99191b681c)

        
    - 해당 CORS 설정에  `configuration.addExposedHeader("Authorization");`를 추가함으로써 문제 해결!

1. 배포 환경(백앤드 → 프론트 통신)
    
    <img width="503" alt="image (7)" src="https://github.com/user-attachments/assets/05998006-0f20-486c-8e88-b22de479c3f3">

    
    - ALB에 80, 443, 8080 리스너를 3개 만들고 80,443 리스너에만 설정해둔 Target Group의 포트가 8080 이였습니다. 하지만 8080 리스너에는 Target Group이 따로 설정되어있지않아 요청이 제대로 처리되지 않았습니다.
    - 현재 8080 리스너를 삭제하고 80, 443 리스너에 80 port Target Group을 설정해둠으로써 Cors 에러를 해결하였습니다.
</div>
</details>

<details>
<summary>DB 접근권한 문제 </summary>
<div markdown="1">
<br>
배포를 테스트하던 중 DB에 분명히 잘 연결이 되어있고 계정의 USERNAME과 PASSWORD가 일치함에도 접근권한이 없어서 application 을 실행하는데 오류가 발생

- `root`  계정으로 db에 접근하는게 잘못됨을 인식하지 못하였음. `root` 계정은 데이터베이스에서 모든 권한을 가지고 있으며 일반적으로 알려진 계정이름이기에 해킹에 표적이 되기 쉬우므로 외부에서의 접근을 차단하기위해 localhost로만 접근 가능하게 제한이 걸려있었음.
- 그걸 모르고 `root` 권한을 직접 바꾸려다가 실패하였고 USER를 새로 생성해서 권한을 설정하고 연결함으로써 문제를 해결함.
</div>
</details>

<details>
<summary>소셜 연동 관련 문제 </summary>
<div markdown="1">
<br>
 1. 배포 할 때 Redirect URL이 일치하지 않아 오류창이 발생
    - 시도
        1. 해당 소셜 개발자 페이지로 들어가 Redirect URL 배포 URL로 설정
        2. Vercel, EC2 내부에서 사용하는 환경 변수도 해당 값과 일치
        3. 전부 일치했음에도 문제 발생
        4. 오류가 발생한 콘솔창에서 Redirect URL을 가져와서 오타가 있는지 확인
    - 해결
        - `www.`를 붙여주지 않아 주소를 찾을 수 없어 발생한 것으로 확인.
2. Google 계정 정보 획득 관련
    - 시도
        1. `https://people.googleapis.com/v1/me` 으로 접근
        2. 반환되는 데이터를 확인해보니 의도한 데이터가 아닌 것을 확인
        3. 구글 문서 확인
        4. 문서가 OAuth, People 등 분리되어있어서 필요한 API를 찾기 어려웠음
    - 해결
        - `https://www.googleapis.com/oauth2/v3/userinfo`로 변경
</div>
</details>

<details>
<summary>카카오 지도 관련 문제 </summary>
<div markdown="1">
<br>
요청이 쉴새없이 발생하는 문제
  - 시도
    1. debounce 적용 시도를 했지만 횟수는 줄어도 특정 행동마다 요청이 발생
    2. B6조와 코드 교류를 하며 이벤트가 각자 발생한다는 것 같다, 묶어보는 것은 어떻겠냐는 답변을 받음
  - 해결
    - 컴포넌트가 언마운트될 때 이벤트 리스너 제거
    - 드래그, 중앙 변경, 줌 변경 시 이벤트에 대한 리스너 추가
    - map과 debounced 관련 된 내용이 변경될 때만 이벤트를 다시 실행하도록 변경
</div>
</details>

</div>
