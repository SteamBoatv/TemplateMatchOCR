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

def excuteOcrByFile_PLUS(image, threshold=5,separator='@'):
    ocr = CnOcr()
    output = ""
    result = ocr.ocr(image)

    # 按照文字的纵坐标进行分组
    text_groups = []
    for item in result:
        added = False
        for group in text_groups:
            if abs(group[0]['position'][0][1] - item['position'][0][1]) < threshold:  # 这里设置一个阈值，表示同一行文字的纵坐标差异不超过10个像素
                group.append(item)
                added = True
                break
        if not added:
            text_groups.append([item])

    for group in text_groups:
        group_text = ''.join([item['text'] for item in group])
        output += group_text + separator

    return output

if __name__ == '__main__':
    a = []
    for i in range(1, len(sys.argv)):
        a.append((sys.argv[i]))
    print(excuteOcrByFile_PLUS(a[0]))
#     print(temp(a[0]))