<!DOCTYPE html>
<meta charset="utf-8">

<style type="text/css">

    .line {
        fill: none;
        stroke: #ffab00;
        stroke-width: 3;
    }

    .overlay {
        fill: none;
        pointer-events: all;
    }

    .dot {
        fill: #ffab00;
        stroke: #fff;
    }

    .focus circle {
        fill: none;
        stroke: steelblue;
    }

</style>
<body>
</body>
<script src="https://d3js.org/d3.v5.min.js"></script>
<script>

    var margin = {top: 50, right: 50, bottom: 50, left: 50}
        , width = window.innerWidth - margin.left - margin.right
        , height = window.innerHeight - margin.top - margin.bottom;

    var n = 21;

    var xScale = d3.scaleLinear()
        .domain([0, n - 1])
        .range([0, width]);

    var yScale = d3.scaleLinear()
        .domain([0, 1])
        .range([height, 0]);

    var line = d3.line()
        .x(function (d, i) {
            return xScale(i);
        })
        .y(function (d) {
            return yScale(d.y);
        })
        .curve(d3.curveMonotoneX)

    var dataset = d3.range(n).map(function (d) {
        return {"y": d3.randomUniform(1)()}
    })


    var svg = d3.select("body").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(xScale));


    svg.append("g")
        .attr("class", "y axis")
        .call(d3.axisLeft(yScale));


    svg.append("path")
        .datum(dataset)
        .attr("class", "line")
        .attr("d", line);


    svg.selectAll(".dot")
        .data(dataset)
        .enter().append("circle")
        .attr("class", "dot")
        .attr("cx", function (d, i) {
            return xScale(i)
        })
        .attr("cy", function (d) {
            return yScale(d.y)
        })
        .attr("r", 5)
        .on("mouseover", function (a, b, c) {
            console.log(a)
            this.attr('class', 'focus')
        })
        .on("mouseout", function () {
        })


</script>