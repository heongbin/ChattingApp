# ChattingApp
파이어베이스를 이용해 기본 로그인과 회원가입, 개인간 채팅 구현.
채팅 사용자들 목록,과 그목록을 클릭하면 해당대화방으로 이동, 대화창들을 마지막 대화내용과 함께 대화목록리스트도 표현.

사용자 목록 클릭시 파이어베이스 데이터에 chattingroomid 부분 duplicate되는 것 수정.
for문으로 chattingroomid와 현재 사용자와 상대방의 네임을 갖고있는 것을 비교하는 if문에 else때문에
생김.
