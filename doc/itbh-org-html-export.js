/**
 * Provides a full size view of any image in a new window when clicked
 * on it.
 */
enableImageZoom = function() {
  $('img').click(function() {
    window.open(this.src, "img_large", "width=1024,height=768");
  });
}

removeOrgModeSrcHighlighting = function() {
  $('pre.src').each(function(index, block) {
    var content = $(block).text();
    $(block).html(content);
  });
}

escapeHTMLSourceBlocks = function() {
  $('pre.src-html').each(function(index, block) {
    var htmlContent = $(block).html();
    $(block).text(htmlContent);
  });
}

/**
 * Detects the language on every source block and highlights it except
 * plain text blocks.
 */
highlightSources = function() {
  $('pre.src').each(function(index, block) {
    if ($(block).hasClass('src-txt')) {
      return;
    }
    hljs.highlightBlock(block);
  });
}

addScaffolding = function() {
  var scaffolding = '<!-- Always shows a header, even in smaller screens. --> \
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer mdl-layout--fixed-header"> \
  <header class="mdl-layout__header"> \
    <div class="mdl-layout__header-row"> \
      <!-- Title --> \
      <span class="mdl-layout-title">CAPTION</span> \
      <!-- Add spacer, to align navigation to the right --> \
      <div class="mdl-layout-spacer"></div> \
      <!-- Navigation. We hide it in small screens. --> \
      <nav class="mdl-navigation mdl-layout--large-screen-only"> \
      </nav> \
    </div> \
  </header> \
  <div class="mdl-layout__drawer"> \
    <span class="mdl-layout-title"></span> \
    <nav class="mdl-navigation"> \
      <div class="mdl-navigation__link"><a href="#"><button class="mdl-button mdl-js-button mdl-button--icon"><i class="material-icons">home</i></button></a></div> \
    </nav> \
  </div> \
  <main class="mdl-layout__content"> \
    <div class="page-content"><!-- Your content goes here --></div> \
  </main> \
</div>';

  // hide all sections
  $('div[id^=outline-container].outline-2').hide(0);

  // insert scaffolding
  $('body').prepend(scaffolding);

  // move content into scaffolding
  $('#content').detach().appendTo('div.page-content');

  // move document information into the footer
  $('div.page-content').append('<footer class="mdl-mini-footer"></footer>');
  $('#postamble').detach().appendTo('footer.mdl-mini-footer');

  // set title in drawer
  $('div.mdl-layout__drawer > span.mdl-layout-title').append('<a href="#">' + $($('h1.title')[0].childNodes[0]).clone().text() + '</a>');

  // generate main navigation links
  $('.outline-2').each(function(index, block) {
    var h2 = $(block).find('h2');
    $('div.mdl-layout__drawer nav.mdl-navigation').prepend('<a class="mdl-navigation__link" href="#' + $(h2).attr('id') + '">' + $(h2).text() + '</a>');
  });

  componentHandler.upgradeElement($('div.mdl-layout')[0]);

  // Close drawer on click
  // see https://github.com/google/material-design-lite/issues/1246#issuecomment-153898697
  document.querySelector('.mdl-layout__drawer').addEventListener('click', function () {
    document.querySelector('.mdl-layout__obfuscator').classList.remove('is-visible');
    this.classList.remove('is-visible');
  }, false);
}

listenForNavigationEvents = function() {
  $(window).on('hashchange', function() {
    var hash = window.location.hash.substring(1);
    console.log(hash);

    $('h1.title').hide();
    $('#table-of-contents').hide();
    $('div[id^=outline-container].outline-2').hide(0);

    if (hash.length === 0) {
      $('h1.title').show();
      $('#table-of-contents').show();
    }
    else {
      $('#outline-container-' + hash).show();
    }
  });
}

$(document).ready(function() {
  removeOrgModeSrcHighlighting();
  enableImageZoom();
  escapeHTMLSourceBlocks();
  highlightSources();
  addScaffolding();
  listenForNavigationEvents();

  // TODO handle footnotes
  $('#footnotes').hide();
});
