const PATTERN_DOMANDA = /^D\d{2}:.+$/;
const PATTERN_RISPOSTA = /^[A-Z]\[(\s|X)\].+$/;
const log = console.log;

function mischiaParagrafi() {
  return Word.run(function (context) {
    var paragraphs = context.document.body.paragraphs;
    context.load(paragraphs, "text");

    return context.sync().then(function () {

      var pDomande = [];
      var iDomande = [];
      var paragraphTexts = [];
      var pRitornoACapo;
      var p, d,id;
      for (var i = 0; i < paragraphs.items.length; i++) {
        p = paragraphs.items[i].text.trim();

        if (PATTERN_DOMANDA.test(p)) {
          d = { i: i, p: p, r: []}
          id = {i:i,iRisposte:[]}
          pDomande.push(d);
          iDomande.push(id);
        } else if (PATTERN_RISPOSTA.test(p)) {
          d.r.push({ i: i, p: p });
          id.iRisposte.push(i);
        }

        paragraphTexts.push(paragraphs.items[i].text);
      }

      // Mischia i paragrafi
      iDomande.sort(function (iDomanda) {
        iDomanda.iRisposte.sort(function () {
            return 0.5 - Math.random();
        });
        return 0.5 - Math.random();
      });

      // Aggiorna i paragrafi nel documento
      for (var i = 0; i < iDomande.length; i++) {
        log(i,pDomande[i])
        if(typeof pDomande[i] === 'undefined')continue;
        paragraphs.items[iDomande[i].i].insertText(pDomande[i].p, "Replace");
        for (var y = 0; y < iDomande[i].iRisposte.length; y++) {
            if(typeof pDomande[i].r[y] === 'undefined')continue;
            paragraphs.items[iDomande[i].iRisposte[y]].insertText(pDomande[i].r[y].p, "Replace");
        }
      }

      return context.sync();
    });
  }).catch(function (error) {
    console.log(error);
  });
}

/** Default helper for invoking an action and handling errors. */
function tryCatch(callback) {
  Promise.resolve()
    .then(callback)
    .catch(function (error) {
      console.error(error);
    });
}

tryCatch(mischiaParagrafi);
