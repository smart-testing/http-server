import random

from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)


class Point:

    def __init__(self, x: int, y: int):
        self.x: int = x
        self.y: int = y

    @staticmethod
    def from_coordinates(coordinates: dict):
        return Point(coordinates["x"], coordinates["y"])

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __floordiv__(self, other: int):
        return Point(self.x // other, self.y // other)

    def to_dict(self):
        return {"x": self.x, "y": self.y}


class Element:
    def __init__(self, element: dict):
        self._position: Point = Point.from_coordinates(element["position"])
        self._size: Point = Point.from_coordinates(element["size"])

    def get_center(self):
        return self._position + self._size // 2


class Action:

    def __init__(self, action_type: str, action_position: Point):
        self._type: str = action_type
        self._position: Point = action_position

    @staticmethod
    def tap_element(element: Element):
        return Action("TAP", element.get_center())

    def to_dict(self):
        return {"type": self._type, "position": self._position.to_dict()}


@app.route('/', methods=["POST"])
def generate_action():
    elements: list = request.get_json(cache=False)
    if len(elements) < 1:
        return jsonify()
    index: int = random.randint(0, len(elements) - 1)
    element: Element = Element(elements[index])
    action: Action = Action.tap_element(element)
    return jsonify(action.to_dict())


@app.route('/feedback', methods=["POST"])
def feedback():
    # принимает NetState + статус предыдущей операции
    return ""


if __name__ == '__main__':
    app.run()
