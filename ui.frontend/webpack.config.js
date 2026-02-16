const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src';
const DIST_ROOT = path.resolve(__dirname, 'dist');

module.exports = (env, argv) => {
    const isProduction = argv.mode === 'production';

    return {
        entry: {
            site: SOURCE_ROOT + '/main.js'
        },
        output: {
            filename: 'clientlib-site/js/[name].js',
            path: DIST_ROOT
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: /node_modules/,
                    use: {
                        loader: 'babel-loader',
                        options: {
                            presets: ['@babel/preset-env']
                        }
                    }
                },
                {
                    test: /\.scss$/,
                    use: [
                        MiniCssExtractPlugin.loader,
                        {
                            loader: 'css-loader',
                            options: {
                                url: false,
                                sourceMap: !isProduction
                            }
                        },
                        {
                            loader: 'postcss-loader',
                            options: {
                                postcssOptions: {
                                    plugins: [
                                        require('autoprefixer'),
                                        ...(isProduction ? [require('cssnano')] : [])
                                    ]
                                },
                                sourceMap: !isProduction
                            }
                        },
                        {
                            loader: 'sass-loader',
                            options: {
                                sourceMap: !isProduction
                            }
                        }
                    ]
                },
                {
                    test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|eot|ttf)$/,
                    type: 'asset/resource',
                    generator: {
                        filename: 'clientlib-site/resources/[name][ext]'
                    }
                }
            ]
        },
        plugins: [
            new CleanWebpackPlugin(),
            new MiniCssExtractPlugin({
                filename: 'clientlib-site/css/[name].css'
            }),
            new CopyWebpackPlugin({
                patterns: [
                    {
                        from: path.resolve(__dirname, 'src/resources'),
                        to: path.resolve(DIST_ROOT, 'clientlib-site/resources'),
                        noErrorOnMissing: true
                    }
                ]
            })
        ],
        devtool: isProduction ? false : 'source-map',
        stats: {
            colors: true,
            modules: false,
            children: false
        }
    };
};
