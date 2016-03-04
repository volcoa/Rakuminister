if (typeof String.prototype.endsWith !== 'function') {
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1
    }
}
$(function(){
  var uri = "@request.uri"
  var matched = false
  $('li.menu' ).each(function(idx){
    if($(this ).find('a' ).attr("href" ).endsWith(uri)){
        matched = true
        $(this ).addClass("active")
    }
  })
  if(!matched){
    $('#home-page' ).addClass("active")
  }
});