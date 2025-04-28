export function formatDate(rawDate, format = "DD.MM.YYYY HH.mm.SS") {
    const date = new Date(rawDate);
    const pad = (n) => String(n).padStart(2, '0');
  
    const replacements = {
      YYYY: date.getFullYear(),
      MM: pad(date.getMonth() + 1),
      DD: pad(date.getDate()),
      HH: pad(date.getHours()),
      mm: pad(date.getMinutes()),
      SS: pad(date.getSeconds())
    };

    return format.replace(/YYYY|MM|DD|HH|mm|SS/g, match => replacements[match]);
}