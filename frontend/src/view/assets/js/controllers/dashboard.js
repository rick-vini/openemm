AGN.Lib.Controller.new('dashboard', function() {
  var self = this;
  var newsCounters;
  var config;

  this.addAction({
    click: 'open-mailing-statistics'
  }, function() {
    if (config.mailingId > 0) {
      AGN.Lib.Page.reload(config.mailingStatisticsLinkPattern.replace('{mailing-id}', config.mailingId));
    }
  });

  this.addAction({
    'change': 'update-template-links'
  }, function() {
    self.runInitializer('UpdateDashboardTemplateLinks');
  });

  this.addDomInitializer('dashboard-statistics', function() {
    config = this.config;

    if (config.mailingId > 0) {
      updateCharts(config.mailingId);
    }
  });

  this.addAction({
    'change': 'statistics-select-mailing'
  }, function() {
    config.mailingId = this.el.val();
    updateCharts(config.mailingId);
    return false;
  });

  function updateCharts(mailingId) {
    jQuery.ajax({
      type: "POST",
      url: config.urls.STATISTICS,
      data: {
        mailingId: mailingId
      },
      success: function(data) {
        var rowNames;
        var rowValues;
        var sumValue;

        data = _.merge({
          'common': [['no Data', '0']], // note: translate
          'clickers': [['no Data', '0']], // note: translate
          'clickersPercent': [0], // note: translate
          'openers': [['no Data', '0']], // note: translate
          'openersPercent': [0] // note: translate
        }, data);

        rowNames = [];
        rowValues = [];
        for (var i = 0; i < data['common'].length; i++) {
          var value = parseInt(data['common'][i][1]);
          rowNames.push(data['common'][i][0]);
          rowValues.push(value);
        }

        c3.generate(AGN.Lib.DashboardStatisticsService.data.statChartData.build(rowNames, rowValues));

        rowNames = [];
        rowValues = [];
        sumValue = 0;
        for (var i = 0; i < data['clickers'].length; i++) {
          var value = roundTo(data['clickers'][i][1] * 100, 1);
          rowNames.push(data['clickers'][i][0]);
          rowValues.push(value);
          sumValue += value;
        }

        if (!isNaN(sumValue) && sumValue > 0) {
          for (var i = 0; i < rowValues.length; i++) {
            rowNames[i] += ' ' + roundTo(100 * rowValues[i]/sumValue, 1) + '%';
          }
        }

        var clickChartTitle = roundTo(data['clickersPercent'][0] * 100, 1) + '%*';
        c3.generate(AGN.Lib.DashboardStatisticsService.data.clickChartData.build(clickChartTitle, rowNames, rowValues));

        rowNames = [];
        rowValues = [];
        sumValue = 0;
        for (var i = 0; i < data['openers'].length; i++) {
          var value = roundTo(data['openers'][i][1] * 100, 1);
          rowNames.push(data['openers'][i][0]);
          rowValues.push(value);
          sumValue += value;
        }

        if (!isNaN(sumValue) && sumValue > 0) {
          for (var i = 0; i < rowValues.length; i++) {
            rowNames[i] += ' ' + roundTo(100 * rowValues[i]/sumValue, 1) + '%';
          }
        }

        var viewChartTitle = roundTo(data['openersPercent'][0] * 100, 1) + '%*';
        c3.generate(AGN.Lib.DashboardStatisticsService.data.viewChartData.build(viewChartTitle, rowNames, rowValues));

        AGN.Initializers.Equalizer();
      }
    });
  }

  function roundTo(number, fractionalDigits) {
    return (parseFloat(number).toFixed(fractionalDigits)) * 1.0;
  }

  this.addDomInitializer('dashboard-news-counters', function($elem) {
    var config = AGN.Lib.Helpers.objFromString($elem.data('config'));
    $.ajax({
      url: config.newsCountersUrl
    }).done(function (data) {
      if (data['counters'] != undefined){
        newsCounters = data['counters'];
        // Show popup on login if there are new messages
        updateNewsButtonNumber();
        if (getUnreadCountSafe("MESSAGE") && !$(".modal-news").exists()) {
          showNewsPopup();
        }
      }
    });
  });

  this.addAction({
    click: 'showNewsPopup'
  }, function () {
    showNewsPopup();
  });

  this.addAction({
    click: "chooseNewsType"
  }, function () {
    var type = this.el.parent().data('news-type');
    setNewsDate(type);
  });

  function showNewsPopup(){
    var openedNewsType = 'MESSAGE';
    var newsCount = 0;

    var availableMessagesCount = getAvailableCountSafe("MESSAGE");

    if (availableMessagesCount < 1){
      var wasChanged = false;

      //Get news type with maximum unread number only if there no available MESSAGES
      for (var key in newsCounters){
        if (newsCounters.hasOwnProperty(key)){
          var count = getUnreadCountSafe(key);
          if (count > newsCount){
            newsCount = count;
            openedNewsType = key;
            wasChanged = true;
          }
        }
      }

      //If there are no unread news and there are no any MESSAGE`s we should try to show any available news
      if (!wasChanged){
        for (key in newsCounters){
          if (newsCounters.hasOwnProperty(key)){
            count = getAvailableCountSafe(key);
            if (count > newsCount){
              newsCount = count;
              openedNewsType = key;
            }
          }
        }
      }
    }
    
    AGN.Lib.Modal.createFromTemplate({openedNewsType:openedNewsType}, "modal-news");
    $("#buttonNewsCounter").text("");
    $(".l-news-menu-item").each(function (index) {
      var unread = getUnreadCountSafe($(this).data("news-type"));
      if (unread > 0){
        $(this).find(".l-news-menu-counter").text(unread);
      }else{
        $(this).find(".l-news-menu-counter").html("&nbsp;");
      }
      if ($(this).hasClass("active")){
        setNewsDate($(this).data("news-type"));
      }
    });
    updateNewsButtonNumber();
  }

  /**
   * Ajax request to update news/messages read date
   * @param {String} type NEWS/MESSAGE
     */
  function setNewsDate(type) {    
    if (getUnreadCountSafe(type) > 0) {
      $("#newsCounter" + type).html("&nbsp;");
      $.ajax({
        url: AGN.Lib.Helpers.objFromString($('[data-initializer="dashboard-news-counters"]').data('config')).newsDateUrl,
        data: {
          type: type
        }
      });
      newsCounters[type]["unreadCount"] = 0;
      updateNewsButtonNumber();
    }
  }

  Number.isInteger = Number.isInteger || function(value) {
    return typeof value === "number" && isFinite(value) && Math.floor(value) === value
  };

  /**
   * Calculate number of unread news with type 'NEWS' and sets this number to the news button
   * @returns {number} number of unread news with type 'NEWS'
   */
  function updateNewsButtonNumber() {
    var count = getUnreadCountSafe('NEWS');
    var buttonNewsCounter = $("#buttonNewsCounter");
    if (count > 0) {
      buttonNewsCounter.parent().removeClass("hidden");
      buttonNewsCounter.parent().parent().css("padding-top","4px");
      buttonNewsCounter.parent().parent().css("padding-bottom","4px");
      buttonNewsCounter.text(count);
    } else {
      buttonNewsCounter.parent().addClass("hidden");
      buttonNewsCounter.parent().parent().css("padding-top","5px");
      buttonNewsCounter.parent().parent().css("padding-bottom","5px");
      buttonNewsCounter.text("");
    }
    return count;
  }

  /**
   * Returns unread news count by news type. Performs checks before return.
   * @param {String} type type of news (NEWS/MESSAGE)
   * @returns {Number} number of unread news
   */
  function getUnreadCountSafe(type){
    if (newsCounters != undefined
        && newsCounters[type] != undefined
        && Number.isInteger(newsCounters[type]["unreadCount"])){
      return newsCounters[type]["unreadCount"];
    }
    return 0;
  }

  /**
   * Returns available news count by news type. Performs checks before return.
   * @param {String} type type of news (NEWS/MESSAGE)
   * @returns {Number} number of available news
   */
  function getAvailableCountSafe(type){
    if (newsCounters != undefined
        && newsCounters[type] != undefined
        && Number.isInteger(newsCounters[type]["availableCount"])){
      return newsCounters[type]["availableCount"];
    }
    return 0;
  }
  
  this.addInitializer('UpdateDashboardTemplateLinks', function($scope) {
    if (!$scope) {
      $scope = $(document);
    }

    var $targets = $scope.find('.js-template-link'),
        templateID = $('[data-action="update-template-links"]').val();

    _.each($targets, function(target) {
      var $target = $(target),
          link = $target.attr('href');

      link = link.replace(/templateID=\d+/, 'templateID=' + templateID );

      $target.attr('href', link);
    });

  });

  this.addInitializer('EndlessScroll', function($scope) {
    if (!$scope) {
      $scope = $(document);
    }

    var $endlessScroll = $scope.find('.js-endless-scroll');

    if ($endlessScroll.length < 1){
      return;
    }

    $endlessScroll.each(function (index) {
      var currentPage = 1;
      var currentScroll = $(this);
      currentScroll.endlessScroll({
        pagesToKeep: null,
        fireOnce: true,
        fireDelay: 1000,
        loader: '',
        insertAfter: '.js-endless-scroll .js-endless-scroll-content',
        ceaseFire: function () {
          return (currentScroll.find('.message-list-item-last').length !== 0 )
        },
        callback: function () {
          var jqhxr;

          AGN.Lib.Loader.prevent();

          jqhxr = $.ajax({
            method: 'GET',
            url: currentScroll.data('url'),
            data: {
              page: currentPage + 1
            }
          });

          currentScroll.find('.js-endless-scroll-content').append('<div class="message-list-loader"><i class="icon icon-refresh icon-spin"></i></div>');

          jqhxr.done(function (resp) {
            currentPage += 1;
            currentScroll.find('.js-endless-scroll-content').append(resp);
            currentScroll.find('.message-list-loader').remove();
            AGN.Initializers.IconsDefs(currentScroll);
          });

          return jqhxr;
        }
      })
    });

  })


});
