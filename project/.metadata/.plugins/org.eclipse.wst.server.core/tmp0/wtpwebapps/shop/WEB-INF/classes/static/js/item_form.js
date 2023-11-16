/**
 * 
 */

$(document).ready(function(){
	var errorMessage = [[${errorMessage}]]; //상품 등록 시 실패 메시지를 받아서 상품 등록 페이지에 재진입 시 alert를 통해 실패 사유를 보여줌
	if(errorMessage != null){
		alert(errorMessage);
	}
	
	bindDomEvent();
});

function bindDomEvent(){
	$(".custom-file-input").on("change", function() {
		var fileName = $(this).val().split("\\").pop(); // 이미지 파일명
		var fileExt = fileName.substring(fileName.lastIndexOf(".")+1); //확장자 추출
		fileExt = fileExt.toLowerCase(); //소문자 변환
		
		
		//파일 첨부 시 이미지 파일인지 검사
		//보통 데이터를 검증할 때는 스크립트에 validationd을 한번 하고 스크립트는 사용자가 변경이 가능하므로 서버에서 한번 더 validation을 한다.
		//스크립트에서 validation을 하는 이유는 서버쪽으로 요청을 하면 네트워크를 통해 서버에 요청이 도착하고 다시 그 결과를 클라이언트에 반환하는 등 리소스를 소모하기 때문
		if(fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif" && fileExt != "png" && fileExt != "bmp"){ 
			
			alert("이미지 파일만 등록이 가능합니다.");
			return;
		}
		
		// label 태그 안의 내용을 jQuery의 .html()을 이용하여 파일명을 입력해준다.
		$(this).siblings(".custom-file-label").html(fileName);
	});
}