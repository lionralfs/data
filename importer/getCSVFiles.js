const cheerio = require('cheerio');

module.exports = htmlString => {
  const $ = cheerio.load(htmlString);
  return $('tr a')
    .toArray()
    .map(el => (el && el.attribs && typeof el.attribs.href === 'string' ? el.attribs.href : false))
    .filter(el => {
      // only grab sds011 sensors
      return el && /_(sds011_sensor)_.+\.csv$/.test(el);
    });
};
