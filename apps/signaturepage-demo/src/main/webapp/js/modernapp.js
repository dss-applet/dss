$(document).ready(function(){
	window.scrollTo(0,1);
	//$('.toggle-menu').bind('click', function(){ toggleMenu(); })	
	
	$('.first-menu>li>a').bind('click',function(){showLv2Menu(this);});	
	$('.fixed-first-menu>li>a').bind('click',function(){showLv2FixedMenu(this);});	
	$('a.closeMenu').bind('click',function(){closeMenu();});
	
	$('.sub-menu>li>a').bind('click',function(){ 
		$('.sub-menu a.selected').removeClass('selected');
		$(this).addClass('selected');
	});	
	
	$('a.switch-fixed-menu').bind('click',function(){ toggleFixedMenu(this); })
	
	
	
});


function switchToSignup(){
	$('.pt-page-1').removeClass('pt-page-flipInLeft').addClass('pt-page-flipOutRight'); 
	window.setTimeout(function(){ $('.pt-page-2').css({display:'block'}).addClass('pt-page-flipInLeft'); },500);
	if(navigator.userAgent.toLowerCase().indexOf('msie') > -1)
		$('.pt-page-1').hide();
}
function switchToLogin(){
	$('.pt-page-2').removeClass('pt-page-flipInLeft').addClass('pt-page-flipOutRight'); 
	window.setTimeout(function(){ $('.pt-page-1').css({display:'block'}).addClass('pt-page-flipInLeft'); },500);
	if(navigator.userAgent.toLowerCase().indexOf('msie') > -1)
			$('.pt-page-2').hide();

}


/* --- SLIDING MENU FUNCTIONS ---- */
function showLv1Menu(){
	$('.main-menu').css({left:'0',display:'block'});
	$('.main-menu ul.first-menu').animate({opacity:'1',left:'0'},250);
	$('.closeMenu').css({display:'block'});
}
function showLv2Menu(el){
	$('.first-menu>li>a.selected').removeClass('selected');
	$(el).addClass('selected');
	$('.main-menu').css({width:'490px',left:'0'});
	$('.main-menu ul.sub-menu').css({left:'-490px',opacity:'0.1'});
	$('.main-menu ul.sub-menu#'+$(el).attr('data-sub-menu')).css({left:'190px',opacity:'0.1'});
	$('.main-menu ul.sub-menu#'+$(el).attr('data-sub-menu')).animate({opacity:'1',left:'200px'},250);
	$('.closeMenu').css({display:'block'});
}
function closeMenu(){
	$('.main-menu').animate({left:'-580px'},250,function(){ $(this).css({display:'none'}); });
	$('.closeMenu').css({display:'none'});
	//$('.main-menu ul.first-menu').animate({opacity:'0',left:'-260px'},250);
	//$('.main-menu ul.sub-menu').animate({opacity:'0',left:'-260px'},250);
}


/* ----- FIXED MENU FUNCTIONS ----*/
function toggleFixedMenu(el){
	if( $(el).hasClass('opened') ){
		$(el).removeClass('opened');
		$('.fixed-menu ul.fixed-first-menu').fadeOut(200, function(){ 
				$('.pt-page').animate({left:'0',right:'0'},200);	
		});  
		
	}
	else{
		$(el).addClass('opened');
		$('.pt-page').css({width:'auto',right:'-210px'}); $('.pt-page').animate({left:'210px'},200, function(){ $('.fixed-menu ul.fixed-first-menu').fadeIn(200); }); 
	}
}
function showLv2FixedMenu(el){
	$('.fixed-first-menu>li>a.selected').removeClass('selected');
	$(el).addClass('selected');
	$('.fixed-menu ul.fixed-sub-menu').css({display:'none'});
	$('.fixed-menu ul.fixed-sub-menu#'+$(el).attr('data-sub-menu')).slideDown(200);
}


function showLoader(id){
	var el = $('#'+id);
	if ( el.parent().get(0).tagName.toLowerCase() == "body" ){
		el.css({position:'fixed'}).fadeIn();	
	}
	else{
		el.fadeIn();	
	}
}



