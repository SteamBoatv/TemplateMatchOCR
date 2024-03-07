from cnocr import CnOcr
import sys
import io
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
def excuteOcrByFile(image):
    ocr = CnOcr()
    output = ""
    result = ocr.ocr(image)
    for i in range(len(result)):
        output += result[i]['text']
        output += '@'
    return output

if __name__ == '__main__':
    a = []
    for i in range(1, len(sys.argv)):
        a.append((sys.argv[i]))
    print(excuteOcrByFile(a[0]))
#     print(temp(a[0]))