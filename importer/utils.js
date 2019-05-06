module.exports = {
  zeroPad: num => (num > 9 ? `${num}` : `0${num}`),
  chunkArray: (array, chunk_size) =>
    Array(Math.ceil(array.length / chunk_size))
      .fill()
      .map((_, index) => index * chunk_size)
      .map(begin => array.slice(begin, begin + chunk_size))
};
